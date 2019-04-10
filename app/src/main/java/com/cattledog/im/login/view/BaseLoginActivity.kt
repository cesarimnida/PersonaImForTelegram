package com.cattledog.im.login.view

import android.arch.lifecycle.ViewModelProviders
import com.cattledog.im.common.view.BaseActivity
import com.cattledog.im.login.viewmodel.LoginViewModel

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/03/2019
 * ************************************************************
 */
open class BaseLoginActivity : BaseActivity() {
    override lateinit var viewModel: LoginViewModel

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }
}