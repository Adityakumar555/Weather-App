package com.example.weatherapp.repository

import com.example.weatherapp.model.WeatherData
import retrofit2.Response

class WeatherRepository {

     suspend fun getWeatherFromRepository(city: String, key: String): Response<WeatherData> {
        val call = RetrofitInstance.apiCall.getWeatherResponse(city, key)
        return call
    }

}