package com.exam.matengga.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fruitName: String,
    val ripeness: String,
    val imageUri: String,
    val timestamp: Long
)
