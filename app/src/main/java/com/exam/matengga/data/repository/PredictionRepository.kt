package com.exam.matengga.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.matengga.data.response.PredictionResponse
import com.exam.matengga.data.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PredictionRepository {
    fun classifyFruit(file: File): LiveData<Result<PredictionResponse>> {
        val liveData = MutableLiveData<Result<PredictionResponse>>()
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)

        ApiConfig.instance.predictImage(part).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        liveData.postValue(Result.success(body))
                    } else {
                        liveData.postValue(Result.failure(Exception("Response body is null")))
                    }
                } else {
                    liveData.postValue(Result.failure(Exception(response.errorBody()?.string())))
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                liveData.postValue(Result.failure(t))
            }
        })
        return liveData
    }
}
