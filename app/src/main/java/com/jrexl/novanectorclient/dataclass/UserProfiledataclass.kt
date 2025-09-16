package com.jrexl.novanectorclient.dataclass

data class UserProfiledataclass(
    val imageurl: String, val Username: String, val contactno: String
)

data class ProfileDataClass(
    val uname:String,
    val designation: String,
    val ProfilePic : String,
    val gmailid: String,
    val dob: String,
    val contactno: String,
    val address: String,
    val Pincode :Int,
    val State:String,
    val Country: String,
    val PanCardVerify: String,
    val AadharCardVerify : String,
    val BankAccount: String,
    val BankHolderName: String,
    val BankName: String,
    val ifsc: String,

    )