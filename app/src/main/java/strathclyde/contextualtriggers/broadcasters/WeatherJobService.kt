package strathclyde.contextualtriggers.broadcasters

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import strathclyde.contextualtriggers.api.RetrofitBuilder
import strathclyde.contextualtriggers.models.IWeatherResponse

class WeatherJobService : JobService() {
    companion object {
        const val WEATHER_BROADCAST_ID = "strathclyde.contextualtriggers.broadcast.WEATHER"
    }

    private var cachedWeatherResponse: IWeatherResponse? = null

    override fun onStartJob(params: JobParameters?): Boolean {
        if (params != null) {
            doBackgroundWork(params)
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("JOB CANCELLED", "Before completed")
        return true
    }

    private fun doBackgroundWork(jobParameters: JobParameters) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Coroutine", "Stated")
            fetchWeather(
                jobParameters.extras.getDouble("lat"),
                jobParameters.extras.getDouble("lon")
            )
            jobFinished(jobParameters, false)

        }
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val localCachedWeatherResponse = cachedWeatherResponse
            val weatherResponse = RetrofitBuilder.OPEN_WEATHER_API_SERVICE.getWeather(
                lat, lon, "metric", "2c6c4f6e4567938518b066d7122660d1"
            )
            if (weatherResponse.isSuccessful) {
                weatherResponse.body()?.let {
                    if (localCachedWeatherResponse == null || localCachedWeatherResponse != it) {
                        broadcastWeather(it)
                        cachedWeatherResponse = it
                        Log.d("cachedWeatherResponse", " TEMP:${cachedWeatherResponse?.main?.temp}")
                    } else {
                        Log.d("Weather the same as before", "SAME")
                    }
                }
            } else {
                Log.w("Weather call not successful", "${weatherResponse.code()}")
            }

        }
    }

    private fun broadcastWeather(weatherResponse: IWeatherResponse) {
        Intent().also { intent ->
            intent.action = WEATHER_BROADCAST_ID
            intent.putExtra("weather", weatherResponse.weather?.get(0)?.main)
            intent.putExtra("windspeed", weatherResponse.wind?.speed)
            intent.putExtra("clouds", weatherResponse.clouds?.percentage)
            intent.putExtra("temp", weatherResponse.main?.temp)
            application.sendBroadcast(intent)
        }
    }
}