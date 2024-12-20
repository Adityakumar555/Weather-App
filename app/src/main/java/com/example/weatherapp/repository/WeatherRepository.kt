package com.example.weatherapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.RetrofitInstance
import com.example.weatherapp.model.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {

    private val weatherData = MutableLiveData<WeatherData>()

     suspend fun getWeather(city: String, key: String): MutableLiveData<WeatherData> {
        val call = withContext(Dispatchers.IO){ RetrofitInstance.apiCall.getWeather(city, key) }

         if (call.isSuccessful) {
             weatherData.postValue(call.body())
         }else{
             weatherData.postValue(null)
         }

        return weatherData

    }


}