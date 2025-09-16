package com.jrexl.novanectorclient.dataclass

data class Withdrawaldtclss(val requestid : String,
                            val contactcno: String,
                            val profilepic: String,
                            val coinusername: String,
                            val reason: String,
                            val coindate: String,
                            val coins:Number,
                            val status: String)
data class withdrawalresponse( val requestId: String, val contactcno: String , val coins:Int, val  status : String )

data class alltransactiondc( val requestid: String,
                             val username : String,
                             val contactcno: String,
                             val coins: Int,
    val date :String,
                             val userid:String,
                             val itemexp: String,
                             val typetransaction:String
)

data class transactionhistory(
    var contactcno: String,
    var itemexp: String,
    var typetransaction: String,
    var coins: Int,
    var date: String
)

data class CreditCoinRequest(
    val contactcno: String,
    val coins: Int,
    val scanQRCodes: String
)


data class DeductCoinRequest(
    val contactcno: String,
    val coins: Int,
    val reason: String
)

data class Cardinfo(val name: String, val coins: Int, val cardNumber: String)
