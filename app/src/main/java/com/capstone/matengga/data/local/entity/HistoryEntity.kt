package com.capstone.matengga.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUri: String,
    val fruitName: String,
    val ripeness: String,
    val ripenessPercentage: Double,
    val timestamp: Long = System.currentTimeMillis()
)