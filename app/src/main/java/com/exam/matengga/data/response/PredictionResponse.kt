package com.exam.matengga.data.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("fruit_name")
    val predictedFruit: String?,
    @SerializedName("ripeness")
    val ripeness: String?
)
