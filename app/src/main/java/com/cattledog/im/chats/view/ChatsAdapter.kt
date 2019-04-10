package com.cattledog.im.chats.view

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.cattledog.im.R
import com.cattledog.im.chats.viewmodel.ChatsViewModel
import com.cattledog.im.common.viewmodel.BaseViewModel
import com.cattledog.im.message.view.MessageActivity
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.item_chat.view.*
import org.drinkless.td.libcore.telegram.TdApi

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/03/2019
 * ************************************************************
 */
class ChatsAdapter : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    private val chats = ArrayList<TdApi.Chat>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ChatsAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        holder.title.text = chat.title
        val lastMessage = chat.lastMessage
        holder.lastMessage.text = ChatsViewModel.parseLastMessage(lastMessage)
        holder.lastMessageTime.text = ChatsViewModel.parseLastMessageTime(lastMessage)
        setPhoto(holder.photo, chat.photo)
        holder.chat.setOnClickListener { MessageActivity.startActivity(holder.chat.context as Activity, chat) }
    }

    private fun setPhoto(imageView: ImageView, photo: TdApi.ChatPhoto?) {
        if (photo == null || (photo.big == null && photo.small == null)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, BaseViewModel.defaultPhoto()))
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

    fun updateList(chats: ArrayList<TdApi.Chat>?) {
        this.chats.clear()
        this.chats.addAll(chats ?: ArrayList())
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chat = itemView.cl_chat!!
        val photo = itemView.iv_photo_chat!!
        val title = itemView.tv_title_chat!!
        val lastMessage = itemView.tv_last_message_chat!!
        val lastMessageTime = itemView.tv_last_message_time_chat!!
    }
}