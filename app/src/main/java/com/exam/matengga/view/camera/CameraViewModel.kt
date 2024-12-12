package com.exam.matengga.view.camera

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()

    fun setImageUri(uri: Uri?) {
        Log.d("CameraViewModel", "Image URI set to: $uri")
        _imageUri.value = uri
    }
}