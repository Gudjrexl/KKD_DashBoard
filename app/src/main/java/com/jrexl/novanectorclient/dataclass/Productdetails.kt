package com.jrexl.novanectorclient.dataclass

data class Productdetails(
    val prodid: Int,
    val Prodtname: String,
    val Prodtdescription: String,
    val Prodtprice: Int,
    val ProdtstockQuantity: Int,
    val Prodtcategory: String,
    val Productimg : String,
    val ProductCoin: Int,
    val qr: String
)

data class Productdetailsadd(
    val prodid: Int,
    val Prodtname: String,
    val Prodtdescription: String,
    val Prodtprice: Int,
    val ProdtstockQuantity: Int,
    val Prodtcategory: String,
    val ProductCoin: Int,
    val qr: String
)



data class categorydatclss( val name: String, val ctimg: String)
data class categorydatclsspost( val name: String)


