package com.exam.matengga.view.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exam.matengga.data.repository.PredictionRepository

class ResultViewModelFactory(private val repository: PredictionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
