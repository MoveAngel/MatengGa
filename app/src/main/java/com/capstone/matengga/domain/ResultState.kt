package com.capstone.matengga.domain

import com.capstone.matengga.data.model.PredictionResponse

sealed class ResultState {
    data object Loading : ResultState()
    data class Success(val data: PredictionResponse) : ResultState()
    data class Error(val error: String) : ResultState()
}