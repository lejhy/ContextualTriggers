package strathclyde.contextualtriggers.broadcasters

import android.app.Application
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import strathclyde.contextualtriggers.api.RetrofitBuilder
import strathclyde.contextualtriggers.models.WeatherResponse


class WeatherBroadcast(
    private val application: Application
) {
    companion object {
        const val WEATHER_BROADCAST_ID = "strathclyde.contextualtriggers.broadcast.WEATHER"
    }

    val mainHandler = Handler(Looper.getMainLooper())

    fun start() {
        mainHandler.post(object : Runnable {
            override fun run() {
                val location = getLocation()
                if (location != null) {
                    CoroutineScope(IO).launch {
                        val weatherResponse = RetrofitBuilder.weatherApiService.getWeather(
                            location.latitude,
                            location.longitude,
                            "metric",
                            "2c6c4f6e4567938518b066d7122660d1"
                        )
                        sendWeatherBroadcast(weatherResponse)
                    }
                }
                mainHandler.postDelayed(this, 60000)
            }
        })
        Log.d("Weather Broadcast ", "Started")


    }

    private fun getLocation(): Location? {
        if (
            ContextCompat.checkSelfPermission(
                application,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                application,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val lm = application.getSystemService(LOCATION_SERVICE) as LocationManager?
            return lm?.getLastKnownLocation(GPS_PROVIDER)
        }
        return null
    }

    private fun sendWeatherBroadcast(weatherResponse: WeatherResponse) {
        Intent().also { intent ->
            intent.action = WEATHER_BROADCAST_ID
            intent.putExtra("weather", weatherResponse.weather?.get(0)?.main)
            intent.putExtra("windspeed", weatherResponse.wind?.speed)
            intent.putExtra("clouds", weatherResponse.clouds?.all)
            intent.putExtra("temp", weatherResponse.main?.temp)
            application.sendBroadcast(intent)
        }
    }
}