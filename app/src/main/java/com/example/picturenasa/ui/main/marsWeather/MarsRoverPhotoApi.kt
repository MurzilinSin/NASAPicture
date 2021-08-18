package com.example.picturenasa.ui.main.marsWeather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface MarsRoverPhotoApi {
    @GET("mars-photos/api/v1/rovers/{rover}/photos")
        fun getMarsPhoto(@Path("rover")rover: String,
                         @Query("earth_date")date: String,
                         @Query("api_key")apiKey : String) : Call<MRPServerResponseData>
}