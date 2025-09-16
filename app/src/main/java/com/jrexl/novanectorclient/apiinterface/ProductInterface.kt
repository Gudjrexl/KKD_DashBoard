package com.jrexl.novanectorclient.apiinterface

import com.jrexl.novanectorclient.dataclass.Productdetails
import com.jrexl.novanectorclient.dataclass.categorydatclss
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductInterface {
    @GET("productdetails/productdet")
    suspend fun GetProductDetl(): List<Productdetails>

    @Multipart
    @POST("productdetails/productdet")
    suspend fun addProduct(
        @Part("prodid") prodid: RequestBody,
        @Part("Prodtname") prodtname: RequestBody,
        @Part("Prodtdescription") description: RequestBody,
        @Part("Prodtprice") price: RequestBody,
        @Part("ProdtstockQuantity") stockQuantity: RequestBody,
        @Part("Prodtcategory") category: RequestBody,
        @Part("ProductCoin") productCoin: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @DELETE("productdetails/productdet/{Prodtname}")
    suspend fun deleteProduct(
        @Path("Prodtname") productName: String
    ): Response<Unit>


    @DELETE("client/category/{name}")
    suspend fun deleteCategory(
        @Path("name") categoryName: String
    ): Response<Unit>

    @GET("client/category")
    suspend fun GetCategory(): List<categorydatclss>


    @Multipart
    @POST("client/category")
    suspend fun AddCategory(
        @Part("name") name: RequestBody,
        @Part ctimg: MultipartBody.Part?
    ): Response<Unit>



}