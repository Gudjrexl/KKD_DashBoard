package com.jrexl.novanectorclient.objectapi

import com.jrexl.novanectorclient.apiinterface.ProductInterface
import com.jrexl.novanectorclient.apiinterface.offerPromointerface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Offerpromoretro {

    private const val BASE_URL = Urlobject.BASE_URL

    val offerapi: offerPromointerface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(offerPromointerface::class.java)
    }
}