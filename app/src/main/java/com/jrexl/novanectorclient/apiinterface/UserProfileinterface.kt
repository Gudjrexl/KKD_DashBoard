package com.jrexl.novanectorclient.apiinterface

import com.jrexl.novanectorclient.dataclass.ProfileDataClass
import com.jrexl.novanectorclient.dataclass.UserProfiledataclass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserProfileinterface {

    @GET("user/profiledet")
    suspend fun clientprofile(): List<UserProfiledataclass>


    @GET("user/profiledata/{contactno}")
    suspend fun Profileget(@Path("contactno") contactno: String?): Response<ProfileDataClass>
}