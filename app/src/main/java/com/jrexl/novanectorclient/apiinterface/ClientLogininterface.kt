package com.jrexl.novanectorclient.apiinterface

import com.jrexl.novanectorclient.dataclass.clientlogin
import com.jrexl.novanectorclient.dataclass.lofinresponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ClientLogininterface {
    @POST("client/login")
    suspend fun clientlog(@Body clientlogin: clientlogin): Response<lofinresponse>
}