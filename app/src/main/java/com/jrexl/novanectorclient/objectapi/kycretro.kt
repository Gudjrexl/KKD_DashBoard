package com.jrexl.novanectorclient.objectapi

import com.jrexl.novanectorclient.apiinterface.Kycinterface
import com.jrexl.novanectorclient.apiinterface.ProductInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object kycretro {
    private const val BASE_URL = Urlobject.BASE_URL

    val kycapi: Kycinterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Kycinterface::class.java)
    }
}