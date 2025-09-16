package com.jrexl.novanectorclient.objectapi

import com.jrexl.novanectorclient.apiinterface.UserProfileinterface
import com.jrexl.novanectorclient.apiinterface.Withdrawalinterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Withdrawalobject {
    private const val BASE_URL = Urlobject.BASE_URL

    val api: Withdrawalinterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Withdrawalinterface::class.java)
    }
}