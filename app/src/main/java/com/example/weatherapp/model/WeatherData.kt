package com.example.weatherapp.model

data class WeatherData(

    val name: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class Wind(
    val speed: Double
)

data class Main(
    val humidity: Int
    )

data class Weather(
    val main: String
)


