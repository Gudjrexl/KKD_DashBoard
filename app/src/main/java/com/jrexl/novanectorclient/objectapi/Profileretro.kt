package com.jrexl.novanectorclient.objectapi

import com.jrexl.novanectorclient.apiinterface.UserProfileinterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object Profileretro {

    private const val BASE_URL = Urlobject.BASE_URL

    val api: UserProfileinterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserProfileinterface::class.java)
    }
}