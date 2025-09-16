package com.jrexl.novanectorclient.dataclass

data class HomePagedataclass(val totalUser: Int, val totalProduct: Int, val kycRequest: Int, val withdrawalRequest: Int, val category: Int,val promotionCount:Int, val offer:Int)

data class Hometransactiondataclass(
    val userid: String,
    val username: String,
    val contactcno: String,
    val itemexp: String,
    val typetransaction: String,
    val date: String,
    val coins: Int
)

data class qrcheckdb(val exists: Boolean)