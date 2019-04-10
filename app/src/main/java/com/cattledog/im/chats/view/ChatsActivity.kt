package com.cattledog.im.chats.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.cattledog.im.R
import com.cattledog.im.chats.viewmodel.ChatsViewModel
import com.cattledog.im.common.view.BaseActivity
import kotlinx.android.synthetic.main.activity_chats.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 28/03/2019
 * ************************************************************
 */
class ChatsActivity : BaseActivity() {
    override lateinit var viewModel: ChatsViewModel
    private val adapter = ChatsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        rv_chats.layoutManager = LinearLayoutManager(this)
        rv_chats.adapter = adapter
        viewModel.orderedChats.observe(this, Observer { chats -> adapter.updateList(chats) })
        viewModel.getChats()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatsViewModel::class.java)
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, ChatsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
        }
    }
}