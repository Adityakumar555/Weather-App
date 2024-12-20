package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.repository.WeatherRepository

class WeatherViewModel : ViewModel() {

    private val weatherRepository:WeatherRepository by lazy {
        WeatherRepository()
    }

    suspend fun getWeather(cityName: String, key: String): LiveData<WeatherData> {
        return weatherRepository.getWeather(cityName, key)
    }


}