package com.cattledog.im.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.cattledog.im.R
import kotlinx.android.synthetic.main.activity_password.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 01/04/2019
 * ************************************************************
 */
class PasswordActivity : BaseLoginActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        bt_password.setOnClickListener {
            val password = et_password.text.toString()
            if (password.isBlank()) return@setOnClickListener
            viewModel.checkAuthenticationPassword(password)
        }
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, PasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }
}