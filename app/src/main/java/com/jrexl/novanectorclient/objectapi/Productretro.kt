package com.jrexl.novanectorclient.objectapi

import com.jrexl.novanectorclient.apiinterface.ProductInterface
import com.jrexl.novanectorclient.apiinterface.UserProfileinterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Productretro {
    private const val BASE_URL = Urlobject.BASE_URL

    val Productapi: ProductInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductInterface::class.java)
    }
}