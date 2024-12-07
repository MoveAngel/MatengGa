// data/local/room/MatengGaDatabase.kt
package com.capstone.matengga.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.matengga.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1)
abstract class MatengGaDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: MatengGaDatabase? = null

        fun getInstance(context: Context): MatengGaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MatengGaDatabase::class.java,
                    "matengga.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}