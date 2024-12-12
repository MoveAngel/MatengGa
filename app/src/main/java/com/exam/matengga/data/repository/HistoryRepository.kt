package com.exam.matengga.data.repository

import androidx.lifecycle.LiveData
import com.exam.matengga.data.local.entity.HistoryEntity
import com.exam.matengga.data.local.room.HistoryDao

class HistoryRepository(private val dao: HistoryDao) {

    val allHistory: LiveData<List<HistoryEntity>> = dao.getAllHistory()

    suspend fun insertHistory(history: HistoryEntity) {
        dao.insertHistory(history)
    }

    suspend fun deleteHistory(history: HistoryEntity) {
        dao.deleteHistory(history)
    }

    suspend fun deleteAllHistory() {
        dao.deleteAllHistory()
    }
}
