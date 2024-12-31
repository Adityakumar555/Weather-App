package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherRepository: WeatherRepository by lazy { WeatherRepository() }

    private val setWeatherData = MutableLiveData<WeatherData>()

    val weatherData: LiveData<WeatherData> = setWeatherData


    fun getWeatherFromViewModel(cityName: String, key: String) {
        viewModelScope.launch {
            val call = weatherRepository.getWeatherFromRepository(cityName, key)
            setWeatherData.postValue(call.body())
        }

    }

}