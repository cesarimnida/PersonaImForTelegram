package com.cattledog.im.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.cattledog.im.R
import kotlinx.android.synthetic.main.activity_auth_code.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 28/03/2019
 * ************************************************************
 */
class AuthCodeActivity : BaseLoginActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_code)
        bt_auth_code.setOnClickListener {
            val code = et_auth_code.text.toString()
            if (code.isBlank()) return@setOnClickListener
            viewModel.checkAuthenticationCode(code)
        }
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, AuthCodeActivity::class.java)
            activity.startActivity(intent)
        }
    }
}