package com.cattledog.im.message.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.cattledog.im.R
import com.cattledog.im.common.view.BaseActivity
import com.cattledog.im.message.viewmodel.MessageViewModel
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.content_type_box_message.*
import org.drinkless.td.libcore.telegram.TdApi

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/03/2019
 * ************************************************************
 */
class MessageActivity : BaseActivity() {
    override lateinit var viewModel: MessageViewModel
    private val layoutManager = LinearLayoutManager(this)
    private val adapter = MessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        rv_message.layoutManager = layoutManager
        rv_message.adapter = adapter
        rv_message.addOnScrollListener(scrollListener())
        adapter.viewModel = viewModel
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        viewModel.getChatHistory(intent?.extras?.getSerializable(CHAT) as TdApi.Chat)
        viewModel.getMe()
        viewModel.messages.observe(this, Observer { messages -> adapter.updateList(messages) })
        viewModel.updatedMessage.observe(this, Observer { container -> adapter.updateItem(container) })
        viewModel.users.observe(this, Observer { users -> adapter.updateUsers(users!!) })
        viewModel.currentUser.observe(this, Observer { currentUser -> adapter.updateCurrentUser(currentUser!!) })
        bt_send_type_box_message.setOnClickListener { resetTextAndSendMessage() }
    }

    private fun resetTextAndSendMessage() {
        val message = et_type_box_message.text.toString()
        et_type_box_message.setText("")
        viewModel.sendMessage(message)
    }

    private fun scrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val itemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                viewModel.loadMoreChat(itemCount, lastVisibleItem, adapter.getItem(itemCount - 1))
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)
    }

    companion object {
        private const val CHAT = "com.cattledog.im.message.view.MessageActivity.CHAT"
        fun startActivity(activity: Activity, chat: TdApi.Chat) {
            val intent = Intent(activity, MessageActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(CHAT, chat)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}
