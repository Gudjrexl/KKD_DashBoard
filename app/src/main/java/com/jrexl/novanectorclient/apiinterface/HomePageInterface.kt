package com.jrexl.novanectorclient.apiinterface

import com.jrexl.novanectorclient.dataclass.HomePagedataclass
import com.jrexl.novanectorclient.dataclass.Hometransactiondataclass
import com.jrexl.novanectorclient.dataclass.ProfileDataClass
import com.jrexl.novanectorclient.dataclass.qrcheckdb

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomePageInterface {
    @GET("client/Homedata")
    suspend fun gethomedata(): HomePagedataclass

    @GET("coin/transaction")
    suspend fun hometransactiondewtails(): List<Hometransactiondataclass>

    @GET("coin/download")
    suspend fun downloadTransactions(): Response<ResponseBody>

    @GET("coin/qrcheck/{scanQRCodes}")
    suspend fun checkqrcodes(
        @Path("scanQRCodes") scanQRCodes: String
    ): Response<qrcheckdb>





}