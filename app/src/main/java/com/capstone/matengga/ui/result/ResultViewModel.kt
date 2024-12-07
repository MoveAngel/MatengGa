package com.capstone.matengga.ui.result

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.matengga.data.model.PredictionResponse
import com.capstone.matengga.domain.ResultState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel() {
    private val _predictionState = MutableLiveData<ResultState>()
    val predictionState: LiveData<ResultState> get() = _predictionState

    fun predictImage(uri: Uri) {
        viewModelScope.launch {
            _predictionState.value = ResultState.Loading
            try {
                // Simulasi delay network
                delay(1500)
                _predictionState.value = ResultState.Success(
                    PredictionResponse(
                        fruitName = "apple",
                        ripeness = "85%",
                        ripenessPercentage = 85.0
                    )
                )
            } catch (e: Exception) {
                _predictionState.value = ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }
}