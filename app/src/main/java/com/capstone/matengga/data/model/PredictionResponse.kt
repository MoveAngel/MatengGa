// data/model/PredictionResponse.kt
package com.capstone.matengga.data.model

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("Nama Buah")
    val fruitName: String,
    @SerializedName("Rippenes")   // Sesuaikan dengan response API
    val ripeness: String,         // Berisi "Ripe" atau nilai lainnya
    @SerializedName("Persentase Rippenes")  // Sesuaikan dengan response API
    val ripenessPercentage: Double
) {
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

    fun getDescription(): String {
        val translatedRipeness = getTranslatedRipeness()
        val percentage = String.format("%.1f", ripenessPercentage)
        return "Buah ${getTranslatedFruitName()} $translatedRipeness dengan tingkat kematangan $percentage%"
    }
}