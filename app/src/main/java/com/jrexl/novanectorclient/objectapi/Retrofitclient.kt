package com.jrexl.novanectorclient.objectapi

import com.jrexl.novanectorclient.apiinterface.ClientLogininterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofitclient {
    val baseurl = Urlobject.BASE_URL
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseurl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ClientLogininterface = retrofit.create(ClientLogininterface::class.java)
}