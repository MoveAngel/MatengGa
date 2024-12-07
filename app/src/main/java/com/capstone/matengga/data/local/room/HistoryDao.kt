// data/local/room/HistoryDao.kt
package com.capstone.matengga.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.capstone.matengga.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Insert
    suspend fun insertHistory(history: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun clearHistory()
}