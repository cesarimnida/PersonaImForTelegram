package com.cattledog.im.message.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.cattledog.im.R
import com.cattledog.im.common.model.Item
import com.cattledog.im.common.viewmodel.BaseViewModel
import com.cattledog.im.message.viewmodel.MessageViewModel
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.item_message.view.*
import org.drinkless.td.libcore.telegram.TdApi


/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 02/04/2019
 * ************************************************************
 */
class MessageAdapter : RecyclerView.Adapter<MessageAdapter.UserViewHolder>() {
    private val messages = ArrayList<TdApi.Message>()
    private val users = HashMap<Int, TdApi.User>()
    private var currentUser = TdApi.User()
    lateinit var viewModel: MessageViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        if (viewType == USER_MESSAGE) {
            return MessageAdapter.UserViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_user_message, parent, false)
            )
        }
        return MessageAdapter.ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isMessageFromCurrentUser(position)) USER_MESSAGE else CONTACT_MESSAGE
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val message = getItem(position)!!
        holder.message.position = position
        holder.message.setMessage(message)
        //holder.message.setBackgroundColor(Color.WHITE)
        if (holder is ContactViewHolder) {
            val background = ContactBackground()
            background.intrinsicWidth = holder.message.width
            background.intrinsicHeight = holder.message.height
            holder.message.background = background
            setUserPhoto(holder.photo, message.senderUserId)
        }
    }

    private fun isMessageFromCurrentUser(position: Int): Boolean {
        return currentUser.id == getItem(position)!!.senderUserId
    }

    private fun setUserPhoto(imageView: ImageView, userId: Int) {
        val user = users[userId]
        if (user == null) {
            viewModel.getUser(userId)
            Ion.with(imageView)
                .placeholder(BaseViewModel.defaultPhoto())
                .error(BaseViewModel.defaultPhoto())
                .load(null)
            return
        }
        val photo = user.profilePhoto
        if (photo == null || (photo.big == null && photo.small == null)) {
            Ion.with(imageView)
                .placeholder(BaseViewModel.defaultPhoto())
                .error(BaseViewModel.defaultPhoto())
                .load(null)
            return
        }
        if (photo.big != null) {
            Ion.with(imageView)
                .placeholder(BaseViewModel.defaultPhoto())
                .error(BaseViewModel.defaultPhoto())
                .load(photo.big.local.path)
            return
        }
        Ion.with(imageView)
            .placeholder(BaseViewModel.defaultPhoto())
            .error(BaseViewModel.defaultPhoto())
            .load(photo.small.local.path)
    }

    fun updateList(messages: ArrayList<TdApi.Message>?) {
        this.messages.clear()
        this.messages.addAll(messages ?: ArrayList())
        notifyDataSetChanged()
    }

    fun updateItem(item: Item<TdApi.Message>?) {
        if (item == null) return
        this.messages[item.position] = item.item
        notifyItemChanged(item.position)
    }

    fun updateUsers(users: HashMap<Int, TdApi.User>) {
        this.users.clear()
        this.users.putAll(users)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): TdApi.Message? {
        if (position < 0) return null
        return ArrayList(messages)[position]
    }

    fun updateCurrentUser(currentUser: TdApi.User) {
        this.currentUser = currentUser
    }

    open class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout = itemView.cl_message!!
        val message = itemView.tv_message_message!!
    }

    class ContactViewHolder(itemView: View) : UserViewHolder(itemView) {
        val photo = itemView.iv_photo_message!!
    }

    companion object {
        private const val CONTACT_MESSAGE = 0
        private const val USER_MESSAGE = 1
    }
}