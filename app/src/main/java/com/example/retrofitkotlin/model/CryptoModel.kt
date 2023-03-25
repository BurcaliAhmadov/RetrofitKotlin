package com.example.retrofitkotlin.model

import com.google.gson.annotations.SerializedName

data class CryptoModel (
    @SerializedName("currency")
    var currency:String,
    @SerializedName("price")
    var price: String )
