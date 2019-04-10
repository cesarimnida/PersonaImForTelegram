package com.cattledog.im

import android.app.Application
import timber.log.Timber

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 28/03/2019
 * ************************************************************
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) return
        Timber.plant(Timber.DebugTree())
    }
}