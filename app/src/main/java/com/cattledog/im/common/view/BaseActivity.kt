package com.cattledog.im.common.view

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.cattledog.im.chats.view.ChatsActivity
import com.cattledog.im.common.viewmodel.BaseViewModel
import com.cattledog.im.login.view.AuthCodeActivity
import com.cattledog.im.login.view.PasswordActivity
import com.cattledog.im.login.view.PhoneNumberActivity
import org.drinkless.td.libcore.telegram.TdApi

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/03/2019
 * ************************************************************
 */
abstract class BaseActivity : AppCompatActivity() {
    open val viewModel: BaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        viewModel!!.constructor.observe(this, Observer { constructor -> handleConstructor(constructor) })
        viewModel!!.tdApiError.observe(
            this,
            Observer { message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show() })
    }

    private fun handleConstructor(constructor: Int?) {
        if (constructor == null) return
        when (constructor) {
            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                AuthCodeActivity.startActivity(this)
                finish()
            }
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> ChatsActivity.startActivity(this)
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> PhoneNumberActivity.startActivity(this)
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> PasswordActivity.startActivity(this)
        }
    }

    abstract fun initViewModel()
}