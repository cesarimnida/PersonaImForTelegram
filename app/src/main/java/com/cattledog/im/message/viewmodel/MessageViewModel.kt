package com.cattledog.im.message.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cattledog.im.common.model.Item
import com.cattledog.im.common.viewmodel.BaseViewModel
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 01/04/2019
 * ************************************************************
 */
class MessageViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var chat: TdApi.Chat
    val messages = MutableLiveData<ArrayList<TdApi.Message>>()
    val updatedMessage = MutableLiveData<Item<TdApi.Message>>()
    private val messagesList = ArrayList<TdApi.Message>()
    val users = MutableLiveData<HashMap<Int, TdApi.User>>()
    private val usersHash = HashMap<Int, TdApi.User>()
    private val messageFile = HashMap<Int, Long>()
    private var loading = false

    override fun callback(): Client.ResultHandler {
        return Client.ResultHandler {
            when (it.constructor) {
                TdApi.Message.CONSTRUCTOR -> {
                    val message = it as TdApi.Message
                    handleNewMessage(message)
                }
                TdApi.Messages.CONSTRUCTOR -> {
                    val apiMessages = it as TdApi.Messages
                    handleNewMessages(apiMessages.messages)
                }
                TdApi.UpdateNewMessage.CONSTRUCTOR -> {
                    val updateNewMessage = it as TdApi.UpdateNewMessage
                    handleNewMessage(updateNewMessage.message)
                }
                TdApi.UpdateMessageSendSucceeded.CONSTRUCTOR -> {
                    val updateMessageSendSucceeded = it as TdApi.UpdateMessageSendSucceeded
                    updateMessage(updateMessageSendSucceeded.oldMessageId, updateMessageSendSucceeded.message)
                }
                TdApi.UpdateChatLastMessage.CONSTRUCTOR -> {
                    val updateChatLastMessage = it as TdApi.UpdateChatLastMessage
                    if (updateChatLastMessage.lastMessage == null) return@ResultHandler
                    handleNewMessage(updateChatLastMessage.lastMessage!!)
                }
                TdApi.ConnectionStateReady.CONSTRUCTOR -> {
                    Timber.d("newevent: MessageViewModel %s", it)
                }
                TdApi.UpdateUser.CONSTRUCTOR -> {
                    val updateUser = it as TdApi.UpdateUser
                    val user = updateUser.user
                    usersHash[user.id] = user
                    users.postValue(usersHash)
                }
                TdApi.UpdateUserStatus.CONSTRUCTOR -> {
                    Timber.d("newevent: MessageViewModel %s", it)
                }
                TdApi.UpdateDeleteMessages.CONSTRUCTOR -> {
                    Timber.d("newevent: MessageViewModel %s", it)
                }
                TdApi.UpdateConnectionState.CONSTRUCTOR -> {
                    Timber.d("newevent: MessageViewModel %s", it)
                }
                TdApi.UpdateFile.CONSTRUCTOR -> {
                    val updateFile = it as TdApi.UpdateFile
                    handleFile(updateFile.file)
                }
                TdApi.File.CONSTRUCTOR -> {
                    val file = it as TdApi.File
                    handleFile(file)
                }
                TdApi.User.CONSTRUCTOR -> {
                    val user = it as TdApi.User
                    if (user.profilePhoto == null || user.profilePhoto!!.small == null || user.profilePhoto!!.small.local.path.isBlank()) {
                        client!!.send(TdApi.DownloadFile(user.profilePhoto!!.small.id, 1), callback())
                    }
                    if (user.profilePhoto == null || user.profilePhoto!!.big == null || user.profilePhoto!!.big.local.path.isBlank()) {
                        client!!.send(TdApi.DownloadFile(user.profilePhoto!!.big.id, 2), callback())
                    }
                    usersHash[user.id] = user
                    users.postValue(usersHash)
                }
                else -> {
                    Timber.d("**************************************")
                    Timber.d("still missing on MessageViewModel %s", it)
                    Timber.d("**************************************")
                }
            }
        }
    }

    private fun handleFile(file: TdApi.File) {
        if (file.local.path.isBlank()) return
        val messageId = messageFile[file.id] ?: return
        client!!.send(TdApi.GetMessage(chat.id, messageId), callback())
    }

    private fun updateMessage(oldMessageId: Long, newMessage: TdApi.Message) {
        for (i in 0 until messagesList.size) {
            val message = messagesList[i]
            if (message.id != oldMessageId) continue
            messagesList[i] = newMessage
            updatedMessage.postValue(Item(newMessage, i))
            break
        }
    }

    private fun handleNewMessage(apiMessage: TdApi.Message) {
        handleNewMessages(arrayOf(apiMessage))
        this.messages.postValue(messagesList)
    }

    private fun handleNewMessages(apiMessages: Array<out TdApi.Message>) {
        val messages = HashSet<TdApi.Message>()
        val newMessages = apiMessages.filter { it.chatId == chat.id }
        if (newMessages.isEmpty()) {
            loading = false
            return
        }
        preLoadImages(apiMessages)
        messages.addAll(newMessages)
        messages.addAll(messagesList)
        messagesList.clear()
        messagesList.addAll(messages.sortedWith(compareByDescending { message -> message.date }))
        loading = false
        this.messages.postValue(messagesList)
    }

    private fun preLoadImages(messages: Array<out TdApi.Message>?) {
        if (messages == null) return
        messages.forEach {
            val content = it.content as? TdApi.MessagePhoto ?: return@forEach
            content.photo.sizes.forEach { photoSize -> loadImageIfNeeded(it, photoSize) }
        }
    }

    private fun loadImageIfNeeded(message: TdApi.Message, photoSize: TdApi.PhotoSize) {
        if (photoSize.photo.local.path != null && !photoSize.photo.local.path.isBlank()) return
        messageFile[photoSize.photo.id] = message.id
        client!!.send(TdApi.DownloadFile(photoSize.photo.id, 1), callback())
    }

    fun getChatHistory(chat: TdApi.Chat?) {
        if (chat == null) return
        this.chat = chat
        client!!.send(TdApi.GetChatHistory(chat.id, chat.lastMessage?.id ?: 0L, -20, 30, false), callback())
    }

    private fun userCallback(): Client.ResultHandler? {
        return Client.ResultHandler {
            if (it.constructor != TdApi.User.CONSTRUCTOR) return@ResultHandler
            currentUser.postValue(it as TdApi.User)
        }
    }

    fun getUser(userId: Int) {
        client!!.send(TdApi.GetUser(userId), callback())
    }

    fun buildUserHash(userList: ArrayList<TdApi.User>?): HashMap<Int, TdApi.User> {
        val users = HashMap<Int, TdApi.User>()
        if (userList == null) return users
        userList.forEach { users[it.id] = it }
        return users
    }

    fun getMe() {
        client!!.send(TdApi.GetMe(), userCallback())
    }

    fun loadMoreChat(
        itemCount: Int,
        lastVisibleItem: Int,
        item: TdApi.Message?
    ) {
        if (loading || itemCount >= (lastVisibleItem + 5)) return
        loading = true
        client!!.send(TdApi.GetChatHistory(chat.id, item?.id ?: 0L, -20, 30, false), callback())
    }

    fun sendMessage(message: String) {
        client!!.send(
            TdApi.SendMessage(
                chat.id,
                TdApi.InputMessageText(TdApi.FormattedText(message, null), true, true)
            ),
            callback()
        )
    }
}

