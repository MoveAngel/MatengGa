// data/model/PredictionResponse.kt
package com.capstone.matengga.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@kotlinx.parcelize.Parcelize
data class PredictionResponse(
    @SerializedName("Nama Buah")
    val fruitName: String,
    @SerializedName("Ripeness")
    val ripeness: String,
    @SerializedName("Persentase Ripeness")
    val ripenessPercentage: Double
) : Parcelable {
    fun getTranslatedFruitName(): String {
        return when(fruitName.lowercase()) {
            "apple" -> "Apel"
            "banana" -> "Pisang"
            "durian" -> "Durian"
            "grape" -> "Anggur"
            "mango" -> "Mangga"
            "strawberry" -> "Stroberi"
            "dragonfruit" -> "Buah Naga"
            else -> fruitName
        }
    }

    fun getTranslatedRipeness(): String {
        return when(ripeness.lowercase()) {
            "ripe" -> "Matang"
            "unripe" -> "Belum Matang"
            else -> ripeness
        }
    }

    @SuppressLint("DefaultLocale")
    fun getDescription(): String {
        val translatedRipeness = getTranslatedRipeness()
        val percentage = String.format("%.1f", ripenessPercentage)
        return "Buah ${getTranslatedFruitName()} $translatedRipeness dengan tingkat kematangan $percentage%"
    }
}