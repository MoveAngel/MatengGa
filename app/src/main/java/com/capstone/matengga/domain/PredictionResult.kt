// domain/model/PredictionResult.kt
package com.capstone.matengga.domain.model

data class PredictionResult(
    val fruitName: String,          // nama buah (grape, etc)
    val ripeness: String,           // persentase dalam string (91%)
    val ripenessPercentage: Double, // persentase dalam double (91.89...)
    val description: String         // deskripsi gabungan
)