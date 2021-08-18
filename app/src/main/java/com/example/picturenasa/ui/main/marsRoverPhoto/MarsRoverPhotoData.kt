package com.example.picturenasa.ui.main.marsRoverPhoto

sealed class MarsRoverPhotoData {
    data class Success(val serverResponseData: MRPServerResponseData) : MarsRoverPhotoData()
    data class Error(val error: Throwable) : MarsRoverPhotoData()
    data class Loading(val progress: Int?) : MarsRoverPhotoData()
}