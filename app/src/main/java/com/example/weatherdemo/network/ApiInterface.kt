package com.example.weatherdemo.network

import com.example.weatherdemo.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("v1/forecast")
    fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String,
        @Query("daily") daily: String,
        @Query("current") current: String
    ): Call<Weather>

}