package com.jrexl.novanectorclient.apiinterface

import android.net.Uri
import com.jrexl.novanectorclient.dataclass.OfferDetDatacls
import com.jrexl.novanectorclient.dataclass.OfferResponse
import com.jrexl.novanectorclient.dataclass.Offerpromodtclss
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface offerPromointerface {
    @GET("advertisement/Homeimages")
    suspend fun promo(): Response<List<Offerpromodtclss>>


    @Multipart
    @POST("advertisement/Homeimages")
    suspend fun uploadHomeImage(
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @DELETE("advertisement/Homeimages/{icons}")
    suspend fun deleteHomeImage(
        @Path("icons", encoded = true) icons: String
    ): Response<Unit>



    @GET("offerdetails/offerlist")
    suspend fun GetofferDetl(): List<OfferDetDatacls>


    @DELETE("offerdetails/offerlist/{offername}")
    suspend fun deleteOffer(
        @Path("offername") offerName: String
    ): Response<Unit>

    @Multipart
    @POST("offerdetails/offerlist")
    suspend fun addOffer(
        @Part("offername") offername: RequestBody,
        @Part("offerdescription") offerdescription: RequestBody,
        @Part("offerprice") offerprice: RequestBody,
        @Part oimage: MultipartBody.Part?    // matches multer field name
    ): Response<OfferResponse>

}