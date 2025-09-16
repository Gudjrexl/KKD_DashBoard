package com.jrexl.novanectorclient.apiinterface

import com.jrexl.novanectorclient.dataclass.KycStatusRequest
import com.jrexl.novanectorclient.dataclass.Kycdataclss
import com.jrexl.novanectorclient.dataclass.kyccompletedata
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Kycinterface {
    @GET("user/kycpending")
    suspend fun kycprofile(): List<Kycdataclss>


    @GET("user/kycadpan/{contactno}")
    suspend fun kyccompleted(
        @Path("contactno") contactno: String
    ): kyccompletedata


    @PUT("user/kycadpanstatus/{contactno}")
    suspend fun kycaccept(
        @Path("contactno") contactno: String,
        @Body status: KycStatusRequest
    ): Response<Unit>


}