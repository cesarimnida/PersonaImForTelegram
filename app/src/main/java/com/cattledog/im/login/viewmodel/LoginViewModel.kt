package com.cattledog.im.login.viewmodel

import android.app.Application
import com.cattledog.im.common.viewmodel.BaseViewModel
import org.drinkless.td.libcore.telegram.TdApi

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/03/2019
 * ************************************************************
 */
class LoginViewModel(application: Application) : BaseViewModel(application) {
    fun getAuthorizationState() {
        client!!.send(TdApi.GetAuthorizationState(), callback())
    }

    fun checkAuthenticationCode(code: String) {
        val authCode = TdApi.CheckAuthenticationCode(code, "ss", "se")
        client!!.send(authCode, callback())
    }

    fun setAuthenticationPhoneNumber(phoneNumber: String) {
        val phoneNumberAuthentication = TdApi.SetAuthenticationPhoneNumber(phoneNumber, false, true)
        client!!.send(phoneNumberAuthentication, callback())
    }

    fun checkAuthenticationPassword(password: String) {
        client!!.send(TdApi.CheckAuthenticationPassword(password), callback())
    }
}


