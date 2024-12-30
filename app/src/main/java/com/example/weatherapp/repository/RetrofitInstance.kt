package com.example.weatherapp.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val URL = "https://api.openweathermap.org/data/2.5/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiCall: ApiCall by lazy {
        retrofit.create(ApiCall::class.java)
    }

}