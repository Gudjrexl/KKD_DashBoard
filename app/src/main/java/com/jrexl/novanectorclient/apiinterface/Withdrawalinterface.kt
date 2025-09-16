package com.jrexl.novanectorclient.apiinterface

import com.jrexl.novanectorclient.dataclass.Cardinfo
import com.jrexl.novanectorclient.dataclass.CreditCoinRequest
import com.jrexl.novanectorclient.dataclass.DeductCoinRequest
import com.jrexl.novanectorclient.dataclass.Withdrawaldtclss
import com.jrexl.novanectorclient.dataclass.alltransactiondc
import com.jrexl.novanectorclient.dataclass.transactionhistory
import com.jrexl.novanectorclient.dataclass.withdrawalresponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Withdrawalinterface {

    @GET("client/requestapprove")
    suspend fun getwirhdrawalreq(): List<Withdrawaldtclss>

    @PUT("client/approve")
    suspend fun approvewithdrawal(@Body request: withdrawalresponse) : Response<Unit>

    @GET("coin/alltransaction")
    suspend fun alltransactioninterface(
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<alltransactiondc>


    @GET("/coin/transactions/{contactcno}")
    suspend fun getTransactions(@Path("contactcno") contactcno: String?): List<transactionhistory>

    @POST("/coin/credit")
    suspend fun creditCoin(
        @Body request: CreditCoinRequest
    ): Response<Unit>

    @POST("/coin/deductadmin")
    suspend fun deductCoin(
        @Body request: DeductCoinRequest
    ): Response<Unit>


    @GET("/user/card")
    suspend fun getCardInfo(@Query("phone") phone: String): Cardinfo
}