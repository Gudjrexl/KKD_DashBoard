package com.jrexl.novanectorclient.dataclass

data class Kycdataclss(    val imageurl: String, val Username: String, val contactno: String
)
data class kyccompletedata(
    var contactno: String,
   var  adharno: String,
   var Panno: String,
    var adharimg: String,
   var  panimg: String,
)

data class KycStatusRequest(val kycstatus: String)
