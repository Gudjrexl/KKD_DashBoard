package com.jrexl.novanectorclient.dataclass

import com.google.gson.annotations.SerializedName

data class Offerpromodtclss(val _id: String,
                            @SerializedName("icons") val img: String?)


data class ImageData(
    val _id: String,
    val icons: String
)

data class OfferDetDatacls(
    val offername: String,
    val offerdescription: String,
    val offerprice: Int,
    val Offerimg : String,
)

data class OfferResponse(
    val success: Boolean,
    val message: String,
    val data: Offerdetailsadd
)

data class Offerdetailsadd(
    @SerializedName("_id") val id: String,
    val offername: String,
    val offerdescription: String,
    val offerprice: Int,
    @SerializedName("Offerimg") val offerImg: String
)