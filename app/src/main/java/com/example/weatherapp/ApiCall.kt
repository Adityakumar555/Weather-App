package com.example.weatherapp

import com.example.weatherapp.model.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCall {

/*
//Caused by: java.lang.IllegalArgumentException: URL query string "{q}&{appId}" must not have replace block. For dynamic query parameters use @Query.
    @GET("weather?/{q}&{appid}")
    // @Path is use to replace the placeholder in the url with the dynamic value
    fun getWeather(@Path("q") cityName: String, @Path("appid") appKey: String): Call<Weather>
*/

    @GET("weather?")
    // for dynamic query parameters use @Query
     suspend fun getWeather(@Query("q") cityName: String, @Query("appid") appKey: String): Response<WeatherData>


}