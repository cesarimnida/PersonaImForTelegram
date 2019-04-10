package com.cattledog.im.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.cattledog.im.R
import kotlinx.android.synthetic.main.activity_phone_number.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/03/2019
 * ************************************************************
 */
class PhoneNumberActivity : BaseLoginActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        bt_phone_number.setOnClickListener {
            val phoneNumber = et_phone_number.text.toString()
            if (phoneNumber.isBlank()) return@setOnClickListener
            viewModel.setAuthenticationPhoneNumber(phoneNumber)
        }
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, PhoneNumberActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
        }
    }
}