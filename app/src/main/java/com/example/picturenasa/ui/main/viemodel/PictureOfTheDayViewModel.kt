package com.example.picturenasa.ui.main.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picturenasa.BuildConfig
import com.example.picturenasa.ui.main.picture.PODRetrofitImpl
import com.example.picturenasa.ui.main.picture.PODServerResponseData
import com.example.picturenasa.ui.main.picture.PictureOfTheDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl : PODRetrofitImpl = PODRetrofitImpl()
    ) : ViewModel() {
        fun getData(date: String): LiveData<PictureOfTheDayData> {
            sendServerRequest(date)
            return liveDataForViewToObserve
        }

    private fun sendServerRequest(date: String) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if(apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey,date).enqueue(object :
            Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if(response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if(message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }
            })
        }
    }
}