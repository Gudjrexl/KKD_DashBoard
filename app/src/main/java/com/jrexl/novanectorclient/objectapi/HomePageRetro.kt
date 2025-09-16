package com.jrexl.novanectorclient.objectapi

import com.jrexl.novanectorclient.apiinterface.HomePageInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HomePageRetro {
    private const val BASE_URL = Urlobject.BASE_URL

    val api: HomePageInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HomePageInterface::class.java)
    }
}