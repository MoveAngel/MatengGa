package com.exam.matengga.view.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exam.matengga.data.repository.PredictionRepository
import com.exam.matengga.data.response.PredictionResponse
import java.io.File

class ResultViewModel(private val repository: PredictionRepository) : ViewModel() {

    private val _prediction = MutableLiveData<Result<PredictionResponse>>()
    val prediction: LiveData<Result<PredictionResponse>> = _prediction

    fun classifyFruit(file: File) {
        repository.classifyFruit(file).observeForever {
            _prediction.postValue(it)
        }
    }
}
