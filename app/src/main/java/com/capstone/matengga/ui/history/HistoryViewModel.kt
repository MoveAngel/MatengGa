package com.capstone.matengga.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.matengga.data.local.entity.HistoryEntity
import com.capstone.matengga.data.local.room.HistoryDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyDao: HistoryDao) : ViewModel() {
    private val _historyList = MutableStateFlow<List<HistoryEntity>>(emptyList())
    val historyList: StateFlow<List<HistoryEntity>> = _historyList

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            historyDao.getAllHistory().collect { histories ->
                _historyList.value = histories
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                historyDao.clearHistory()
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }

    class Factory(private val historyDao: HistoryDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HistoryViewModel(historyDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}