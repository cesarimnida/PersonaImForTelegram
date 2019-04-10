package com.cattledog.im.chats.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.text.format.DateUtils.isToday
import com.cattledog.im.common.viewmodel.BaseViewModel
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/03/2019
 * ************************************************************
 */
class ChatsViewModel(application: Application) : BaseViewModel(application) {
    private val chats = ArrayList<TdApi.Chat>()
    val orderedChats = MutableLiveData<ArrayList<TdApi.Chat>>()

    override fun callback(): Client.ResultHandler {
        return Client.ResultHandler {
            when (it.constructor) {
                TdApi.Chats.CONSTRUCTOR -> {
                    val chatIDs = (it as TdApi.Chats).chatIds
                    for (chatID in chatIDs) {
                        client!!.send(TdApi.GetChat(chatID), callback())
                    }
                }
                TdApi.Chat.CONSTRUCTOR -> {
                    val chat = it as TdApi.Chat
                    updateChatList(chat)
                    if (chat.photo == null || chat.photo!!.small == null || chat.photo!!.small.local.path.isBlank()) {
                        client!!.send(TdApi.DownloadFile(chat.photo!!.small.id, 1), callback())
                    }
                    if (chat.photo == null || chat.photo!!.big == null || chat.photo!!.big.local.path.isBlank()) {
                        client!!.send(TdApi.DownloadFile(chat.photo!!.big.id, 2), callback())
                    }
                    this.orderedChats.postValue(orderChats())
                }
                TdApi.UpdateChatLastMessage.CONSTRUCTOR -> {
                    val chat = it as TdApi.UpdateChatLastMessage
                    updateChatLastMessage(chat)
                }
                TdApi.UpdateNewChat.CONSTRUCTOR -> {
                    val chat = (it as TdApi.UpdateNewChat).chat
                    updateNewChat(chat)
                }
                TdApi.UpdateSupergroup.CONSTRUCTOR -> {
                    val supergroup = (it as TdApi.UpdateSupergroup).supergroup
                    updateSuperGroup(supergroup)
                }
                TdApi.Supergroup.CONSTRUCTOR -> {
                    val supergroup = (it as TdApi.Supergroup)
                    updateSuperGroup(supergroup)
                }
                TdApi.BasicGroup.CONSTRUCTOR -> {
                    val group = (it as TdApi.BasicGroup)
                    updateBasicGroup(group)
                }
                TdApi.UpdateBasicGroup.CONSTRUCTOR -> {
                    val group = (it as TdApi.UpdateBasicGroup).basicGroup
                    updateBasicGroup(group)
                }
                TdApi.UpdateUser.CONSTRUCTOR -> {
                    val updateUser = it as TdApi.UpdateUser
                    val user = updateUser.user
                    this.currentUser.postValue(user)
                }
                TdApi.UpdateUserStatus.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                TdApi.UpdateUnreadMessageCount.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                TdApi.UpdateUnreadChatCount.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                TdApi.UpdateScopeNotificationSettings.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                TdApi.UpdateConnectionState.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                TdApi.UpdateChatOrder.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                TdApi.File.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                TdApi.UpdateChatReadInbox.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                TdApi.UpdateNewMessage.CONSTRUCTOR -> {
                    val message = (it as TdApi.UpdateNewMessage).message
                    updateNewMessage(message)
                }
                TdApi.UpdateDeleteMessages.CONSTRUCTOR -> {
                    Timber.d("newevent: ChatsViewModel %s", it)
                }
                else -> {
                    Timber.d("**************************************")
                    Timber.d("still missing on ChatsViewModel: %s", it)
                    Timber.d("**************************************")
                }
            }
        }
    }

    private fun updateNewMessage(newMessage: TdApi.Message) {
        for (i in 0 until chats.size) {
            val chat = chats[i]
            if (chat.id != newMessage.chatId) continue
            chat.lastMessage = newMessage
            chats[i] = chat
            this.orderedChats.postValue(orderChats())
            return
        }
    }

    private fun updateChatLastMessage(updatedChat: TdApi.UpdateChatLastMessage) {
        for (i in 0 until chats.size) {
            val chat = chats[i]
            if (chat.id != updatedChat.chatId) continue
            chat.order = updatedChat.order
            chat.lastMessage = updatedChat.lastMessage
            chats[i] = chat
            this.orderedChats.postValue(orderChats())
            return
        }
        client!!.send(TdApi.GetChat(updatedChat.chatId), callback())
    }

    private fun updateNewChat(updatedChat: TdApi.Chat) {
        for (i in 0 until chats.size) {
            val chat = chats[i]
            if (chat.id != updatedChat.id) continue
            chat.order = updatedChat.order
            chat.lastMessage = updatedChat.lastMessage
            chats[i] = chat
            this.orderedChats.postValue(orderChats())
            return
        }
        client!!.send(TdApi.GetChat(updatedChat.id), callback())
    }

    private fun updateSuperGroup(updatedSupergroup: TdApi.Supergroup) {
        for (i in 0 until chats.size) {
            val chat = chats[i]
            if (chat.id != updatedSupergroup.id.toLong()) continue
            chat.title = updatedSupergroup.username
            chats[i] = chat
            this.orderedChats.postValue(orderChats())
            return
        }
        //client!!.send(TdApi.GetSupergroup(updatedSupergroup.id), callback())
    }

    private fun updateBasicGroup(updatedGroup: TdApi.BasicGroup) {
        for (i in 0 until chats.size) {
            val chat = chats[i]
            if (chat.id == updatedGroup.id.toLong() || chat.id == updatedGroup.upgradedToSupergroupId.toLong()) return
        }
        //client!!.send(TdApi.GetBasicGroup(updatedGroup.id), callback())
    }

    private fun orderChats(): ArrayList<TdApi.Chat> {
        return ArrayList(chats.sortedWith(compareByDescending { it.order }))
    }

    private fun updateChatList(newChat: TdApi.Chat) {
        if (!chats.contains(newChat)) {
            chats.add(newChat)
            return
        }
        for (i in 0 until chats.size) {
            val chat = chats[i]
            if (chat != newChat) continue
            chats[i] = newChat
            break
        }
    }

    fun getChats() {
        client!!.send(TdApi.GetChats(java.lang.Long.MAX_VALUE, 0, 1000), callback())
    }

    companion object {
        fun parseLastMessage(lastMessage: TdApi.Message?): String {
            if (lastMessage == null) return ""
            when (lastMessage.content.constructor) {
                TdApi.MessageText.CONSTRUCTOR -> return trimMessage((lastMessage.content as TdApi.MessageText).text.text.toString())
                TdApi.MessageAnimation.CONSTRUCTOR -> return ""
                TdApi.MessageAudio.CONSTRUCTOR -> return ""
                TdApi.MessageDocument.CONSTRUCTOR -> return ""
                TdApi.MessagePhoto.CONSTRUCTOR -> return ""
                TdApi.MessageExpiredPhoto.CONSTRUCTOR -> return ""
                TdApi.MessageSticker.CONSTRUCTOR -> return ""
                TdApi.MessageVideo.CONSTRUCTOR -> return ""
                TdApi.MessageExpiredVideo.CONSTRUCTOR -> return ""
                TdApi.MessageVideoNote.CONSTRUCTOR -> return ""
                TdApi.MessageVoiceNote.CONSTRUCTOR -> return ""
                TdApi.MessageLocation.CONSTRUCTOR -> return ""
                TdApi.MessageVenue.CONSTRUCTOR -> return ""
                TdApi.MessageContact.CONSTRUCTOR -> return ""
                TdApi.MessageGame.CONSTRUCTOR -> return ""
                TdApi.MessageInvoice.CONSTRUCTOR -> return ""
                TdApi.MessageCall.CONSTRUCTOR -> return ""
                TdApi.MessageBasicGroupChatCreate.CONSTRUCTOR -> return ""
                TdApi.MessageSupergroupChatCreate.CONSTRUCTOR -> return ""
                TdApi.MessageChatChangeTitle.CONSTRUCTOR -> return ""
                TdApi.MessageChatChangePhoto.CONSTRUCTOR -> return ""
                TdApi.MessageChatDeletePhoto.CONSTRUCTOR -> return ""
                TdApi.MessageChatAddMembers.CONSTRUCTOR -> return ""
                TdApi.MessageChatJoinByLink.CONSTRUCTOR -> return ""
                TdApi.MessageChatDeleteMember.CONSTRUCTOR -> return ""
                TdApi.MessageChatUpgradeTo.CONSTRUCTOR -> return ""
                TdApi.MessageChatUpgradeFrom.CONSTRUCTOR -> return ""
                TdApi.MessagePinMessage.CONSTRUCTOR -> return ""
                TdApi.MessageScreenshotTaken.CONSTRUCTOR -> return ""
                TdApi.MessageChatSetTtl.CONSTRUCTOR -> return ""
                TdApi.MessageCustomServiceAction.CONSTRUCTOR -> return ""
                TdApi.MessageGameScore.CONSTRUCTOR -> return ""
                TdApi.MessagePaymentSuccessful.CONSTRUCTOR -> return ""
                TdApi.MessagePaymentSuccessfulBot.CONSTRUCTOR -> return ""
                TdApi.MessageContactRegistered.CONSTRUCTOR -> return ""
                TdApi.MessageWebsiteConnected.CONSTRUCTOR -> return ""
                TdApi.MessagePassportDataSent.CONSTRUCTOR -> return ""
                TdApi.MessagePassportDataReceived.CONSTRUCTOR -> return ""
                TdApi.MessageUnsupported.CONSTRUCTOR -> return ""
                else -> return ""
            }
        }

        private fun trimMessage(message: String): String {
            return if (message.length > 29) (message.substring(0, 29) + "...") else message
        }

        fun parseLastMessageTime(lastMessage: TdApi.Message?): CharSequence? {
            if (lastMessage?.date == null) return ""
            val lastMessageDate = Calendar.getInstance()
            lastMessageDate.timeInMillis = lastMessage.date.toLong() * 1000
            if (isToday(lastMessageDate.timeInMillis)) {
                val formatter = SimpleDateFormat("HH:mm a")
                return formatter.format(lastMessageDate.time)
            }
            if (isSameYear(lastMessageDate)) {
                val formatter = SimpleDateFormat("MMM dd")
                return formatter.format(lastMessageDate.time)
            }
            val formatter = SimpleDateFormat("dd.mm.yy")
            return formatter.format(lastMessageDate.time)
        }

        private fun isSameYear(calendar: Calendar): Boolean {
            val today = Calendar.getInstance()
            return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
        }
    }
}