package com.example.picturenasa.ui.main.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picturenasa.BuildConfig
import com.example.picturenasa.ui.main.marsWeather.MRPRetrofitImpl
import com.example.picturenasa.ui.main.marsWeather.MRPServerResponseData
import com.example.picturenasa.ui.main.marsWeather.MarsRoverPhotoData

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MarsCuriosityPhotoViewModel(
        private val liveDataForViewToObserve: MutableLiveData<MarsRoverPhotoData> = MutableLiveData(),
        private val retrofitImpl : MRPRetrofitImpl = MRPRetrofitImpl()
) : ViewModel() {
    fun getData(nameRover: String,date: String): LiveData<MarsRoverPhotoData> {
        sendServerRequest(nameRover, date)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(nameRover: String,date: String) {
        liveDataForViewToObserve.value = MarsRoverPhotoData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if(apiKey.isBlank()) {
            MarsRoverPhotoData.Error(Throwable("You need API key"))
        } else {
            var sdf = SimpleDateFormat("YYYY-M-d")
            var currentDate = Date()
            val today = sdf.format(currentDate)
            retrofitImpl.getRetrofitImpl().getMarsPhoto(nameRover, date ,apiKey).enqueue(object :
                    Callback<MRPServerResponseData> {
                override fun onResponse(
                        call: Call<MRPServerResponseData>,
                        response: Response<MRPServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value = MarsRoverPhotoData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                    MarsRoverPhotoData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                    MarsRoverPhotoData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<MRPServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = MarsRoverPhotoData.Error(t)
                    println(t)
                }
            })
        }
    }
}