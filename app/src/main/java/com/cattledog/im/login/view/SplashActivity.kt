package com.cattledog.im.login.view

import android.os.Bundle
import com.cattledog.im.R

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 28/03/2019
 * ************************************************************
 */
class SplashActivity : BaseLoginActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAuthorizationState()
    }
}
