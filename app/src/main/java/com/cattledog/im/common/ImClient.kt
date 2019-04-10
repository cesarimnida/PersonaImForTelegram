package com.cattledog.im.common

import org.drinkless.td.libcore.telegram.Client

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 28/03/2019
 * ************************************************************
 */
object ImClient {
    private var client: Client? = null

    fun getClient(callback: Client.ResultHandler): Client? {
        if (client == null) init(callback)
        if (!client!!.containsHandler(callback)) client!!.setUpdatesHandler(callback)
        return client
    }

    private fun init(callback: Client.ResultHandler) {
        client = Client.create(callback, null, null)
    }

}