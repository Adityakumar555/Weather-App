package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        // by default get Moradabad city weather
        lifecycleScope.launch {
            getWeather("Moradabad")
        }

        // Button click listener to fetch weather data
        binding.searchIcon.setOnClickListener {
            val search = binding.searchEditText.text.toString().trim()
            getWeather(search)
        }
    }


    private fun getWeather(search: String) {
        binding.progressBar.visibility = View.VISIBLE
        if (search.isNotEmpty()) {
            lifecycleScope.launch {
                // Observe the weather data
                weatherViewModel.getWeather(search, BuildConfig.API_KEY)
                    .observe(this@MainActivity, Observer { weathers ->
                        if (weathers != null) {
                            weathers.apply {
                                binding.progressBar.visibility = View.GONE
                                binding.temperature.text = "${main.humidity}°"
                                binding.humidity.text = main.humidity.toString()
                                binding.wind.text = wind.speed.toString()
                                binding.weather.text = weather[0].main
                                binding.location.text = name
                            }

                        } else {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@MainActivity,
                                "Please enter correct city name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        } else {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
        }
    }
}
