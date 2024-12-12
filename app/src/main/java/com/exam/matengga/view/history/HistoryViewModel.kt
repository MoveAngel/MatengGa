package com.exam.matengga.view.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.exam.matengga.data.local.entity.HistoryEntity
import com.exam.matengga.data.local.room.MatengGaDatabase
import com.exam.matengga.data.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HistoryRepository
    val allHistory: LiveData<List<HistoryEntity>>

    init {
        val dao = MatengGaDatabase.getDatabase(application).historyDao()
        repository = HistoryRepository(dao)
        allHistory = repository.allHistory
    }

    fun deleteHistory(history: HistoryEntity) = viewModelScope.launch {
        repository.deleteHistory(history)
    }

    fun deleteAllHistory() = viewModelScope.launch {
        repository.deleteAllHistory()
    }
}

