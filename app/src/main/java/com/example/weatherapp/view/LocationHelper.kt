package com.example.weatherapp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale

class LocationHelper(private val context: Activity) {

    // this function check device is connected to internet or not
    fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // function to request permissions
    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    // function to check if permissions are granted or not
    fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    // define the static PERMISSION_REQUEST_ACCESS_LOCATION code
    companion object {
        const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    // check location is enable or not
    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }


    fun getCityName(lat: Double, long: Double): String? {
        val cityName: String?
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        cityName = address?.get(0)?.locality ?: address?.get(0)?.subLocality
        Log.d("TAGi", address?.get(0)?.adminArea.toString())
        Log.d("TAGi", address?.get(0)?.subLocality.toString())
        Log.d("TAGi", address?.get(0)?.locality.toString())
        Log.d("TAGi", cityName.toString())
        return cityName
    }

    // Show a dialog to explain why the permission is necessary and open app settings
    fun showPermissionDeniedDialog() {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Permission Required")
            .setMessage("This app requires location permission to fetch your current weather. Please enable the location permission in the app settings.")
            .setPositiveButton("Go to Settings") { dialog, _ ->
                // Open the app settings screen
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(context, "Permission denied. Cannot fetch location.", Toast.LENGTH_SHORT).show()
            }
            .create()

        dialog.show()
    }

}