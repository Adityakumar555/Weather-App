package com.example.weatherapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.weatherapp.mobile.BuildConfig
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationHelper: LocationHelper

    // Flag to control the display of Toast messages
    private var isErrorToastShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        // Set the current date and day on the UI
        setCurrentDateAndDay()

        // Initialize LocationHelper
        locationHelper = LocationHelper(this)
        // Initialize  FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // get weather on search icon click
        binding.searchIcon.setOnClickListener {
            val search = binding.searchEditText.text.toString().trim()
            getWeather(search) // Fetch weather for the entered city
        }

        // Set swipe-to-refresh listener
        binding.swipeToRefresh.setOnRefreshListener {
            Toast.makeText(this, "Refreshing weather data...", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                getLocation() // Refresh the location and weather data
                binding.swipeToRefresh.isRefreshing = false
            }, 3000)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.searchEditText.text.clear() // Clear the search input
        getLocation() // Fetch the current location
    }

    private fun getLocation() {
        if (locationHelper.isOnline()) { // Check if the device is online
            if (locationHelper.isLocationEnabled()) { // Check if location is enabled
                if (locationHelper.checkPermissions()) { // Check location permissions

                    // Ensure permissions are granted
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }

                    // Fetch the last known location
                    fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                        val location: Location? = task.result
                        if (location != null) {
                            // Get the city name and fetch weather data
                            val currentLocation =
                                locationHelper.getCityName(location.latitude, location.longitude)
                            currentLocation?.let { getWeather(it) }
                            Toast.makeText(
                                this,
                                "Weather Fetched.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Location is unavailable. Please check your GPS settings.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                } else {
                    // Request location permissions if not granted
                    locationHelper.requestPermissions()
                }
            } else {
                // Show dialog if location is disabled
                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle("Location")
                    .setMessage("Please enable your location to fetch weather.")
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Open location") { dialog, _ ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                dialog.show()

            }
        } else {
            // Show a message if the device is offline
            Toast.makeText(this, "Please enable your Internet connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getWeather(search: String) {
        // Check if the device is online
        if (!locationHelper.isOnline()) {
            Toast.makeText(
                this,
                "No internet connection. Please check your network.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE // Show progress bar

        // Check if search input is not empty
        if (search.isNotEmpty()) {
            weatherViewModel.getWeatherFromViewModel(search, BuildConfig.API_KEY)

            // Use observeOnce to ensure observer is attached only once
            weatherViewModel.weatherData.observe(this) { weathers ->
                binding.progressBar.visibility = View.GONE

                if (weathers != null) { // Update UI with weather data
                    binding.temperature.text =
                        "${String.format("%.1f", weathers.main.temp - 273.15)}\u00B0"
                    binding.humidity.text = weathers.main.humidity.toString()
                    binding.wind.text = weathers.wind.speed.toString()
                    binding.weather.text = weathers.weather[0].main
                    binding.location.text = weathers.name
                    // Reset the error toast flag when data is successfully fetched
                    isErrorToastShown = false

                } else {
                    if (!isErrorToastShown) {
                        Toast.makeText(
                            this,
                            "Enter a correct city name.",
                            Toast.LENGTH_SHORT
                        ).show()
                        isErrorToastShown = true // Set the flag to true to avoid multiple toasts
                    }
                }


            }
        } else {
            binding.progressBar.visibility = View.GONE
            // Show a message if no city name is entered
            Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
        }
    }


    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LocationHelper.PERMISSION_REQUEST_ACCESS_LOCATION) {

            /*  grantResults
            PERMISSION_GRANTED (0):
            Indicates that the user granted the requested permission.
            PERMISSION_DENIED (-1):
            Indicates that the user denied the requested permission.*/

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                getLocation()
            } else {
                // Show dialog if permission is permanently denied
                locationHelper.showPermissionDeniedDialog()
            }
        }
    }


    private fun setCurrentDateAndDay() {
        // Set the current date and day on the UI
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = dateFormat.format(calendar.time)
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val day = dayFormat.format(calendar.time)
        binding.date.text = date
        binding.day.text = day
    }
}
