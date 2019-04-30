package com.cattledog.im.common.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Build
import com.cattledog.im.App
import com.cattledog.im.BuildConfig
import com.cattledog.im.R
import com.cattledog.im.common.ImClient
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 29/03/2019
 * ************************************************************
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val resources = application.resources!!
    val constructor = MutableLiveData<Int>()
    val tdApiError = MutableLiveData<String>()
    val currentUser = MutableLiveData<TdApi.User>()
    val client = ImClient.getClient(callback())

    open fun callback(): Client.ResultHandler {
        return object : Client.ResultHandler {
            override fun onResult(obj: TdApi.Object) {
                when (obj.constructor) {
                    TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                        Timber.d("onResult: UpdateAuthState")
                        onAuthStateUpdated((obj as TdApi.UpdateAuthorizationState).authorizationState)
                    }
                    TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                        Timber.d("onResult: TDlibParams")
                        client!!.send(TdApi.SetTdlibParameters(authStateRequest()), this)
                    }
                    TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                        client!!.send(TdApi.CheckDatabaseEncryptionKey(), this)
                        val authState = TdApi.GetAuthorizationState()
                        client.send(authState, this)
                    }
                    TdApi.UpdateConnectionState.CONSTRUCTOR -> when ((obj as TdApi.UpdateConnectionState).state.constructor) {
                        TdApi.ConnectionStateReady.CONSTRUCTOR -> Timber.d("onResult: ConnectionStateReady")
                    }
                    TdApi.Error.CONSTRUCTOR -> {
                        val error = (obj as TdApi.Error)
                        if (error.code == 406 || error.code == 401) return
                        tdApiError.postValue(error.message)
                    }
                    TdApi.Ok.CONSTRUCTOR -> {
                    }
                    TdApi.UpdateOption.CONSTRUCTOR -> {
                    }
                    TdApi.UpdateUser.CONSTRUCTOR -> {
                        val updateUser = obj as TdApi.UpdateUser
                        currentUser.postValue(updateUser.user)
                    }
                    else -> constructor.postValue(obj.constructor)
                }
            }

            private fun onAuthStateUpdated(authorizationState: TdApi.AuthorizationState) {
                when (authorizationState.constructor) {
                    TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                        Timber.d("onResult: TDlibParams")
                        client!!.send(TdApi.SetTdlibParameters(authStateRequest()), this)
                    }
                    TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> client!!.send(
                        TdApi.CheckDatabaseEncryptionKey(),
                        this
                    )
                    TdApi.AuthorizationStateClosed.CONSTRUCTOR -> {
                    }
                    TdApi.AuthorizationStateClosing.CONSTRUCTOR -> {
                    }
                    TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> {
                    }
                    else -> constructor.postValue(authorizationState.constructor)
                }
            }
        }
    }

    private fun authStateRequest(): TdApi.TdlibParameters {
        val authStateRequest = TdApi.TdlibParameters()
        authStateRequest.apiId = BuildConfig.APP_ID
        authStateRequest.apiHash = BuildConfig.APP_HASH
        authStateRequest.useMessageDatabase = true
        authStateRequest.useSecretChats = true
        authStateRequest.systemLanguageCode = "en"
        authStateRequest.databaseDirectory = getApplication<App>().filesDir.absolutePath
        authStateRequest.deviceModel = Build.MODEL
        authStateRequest.systemVersion = Build.VERSION.RELEASE
        authStateRequest.applicationVersion = BuildConfig.VERSION_NAME
        authStateRequest.enableStorageOptimizer = true
        return authStateRequest
    }

    fun dpToPixels(dps: Int): Int {
        val scale = resources.displayMetrics.density
        return (dps * scale + 0.5f).toInt()
    }

    companion object {
        fun defaultPhoto(): Int {
            return R.drawable.ic_add
        }
    }
}