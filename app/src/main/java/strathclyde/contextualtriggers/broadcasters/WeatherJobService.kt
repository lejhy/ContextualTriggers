package strathclyde.contextualtriggers.broadcasters

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import strathclyde.contextualtriggers.api.OpenWeatherRetrofitBuilder
import strathclyde.contextualtriggers.api.WeatherStackRetrofitBuilder
import strathclyde.contextualtriggers.models.IWeather

class WeatherJobService : JobService() {
    companion object {
        const val WEATHER_BROADCAST_ID = "strathclyde.contextualtriggers.broadcast.WEATHER"
    }

    private var cachedWeatherResponse: IWeather? = null

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
            fetchOpenWeather(
                jobParameters.extras.getDouble("lat"),
                jobParameters.extras.getDouble("lon")
            )
            jobFinished(jobParameters, false)
        }
    }

    private fun fetchOpenWeather(lat: Double, lon: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val localCachedWeatherResponse = cachedWeatherResponse
            val weatherResponse = OpenWeatherRetrofitBuilder.OPEN_WEATHER_API_SERVICE.getWeather(
                lat, lon, "metric", "2c6c4f6e4567938518b066d7122660d1"
            )
            if (weatherResponse.isSuccessful) {
                weatherResponse.body()?.let {
                    if (localCachedWeatherResponse == null || localCachedWeatherResponse != it) {
                        broadcastWeather(it)
                        cachedWeatherResponse = it
                    }
                }
            } else {
                Log.w("OpenWeather call not successful", "${weatherResponse.code()}")
//                fetchWeatherStack(lat, lon) //TODO uncomment when submit - free plan allows only 1000 requests a month
            }
        }
    }

    private fun fetchWeatherStack(lat: Double, lon: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val localCachedWeatherResponse = cachedWeatherResponse
            val weatherResponse = WeatherStackRetrofitBuilder.WEATHER_STACK_API_SERVICE.getWeather(
                "2e45e389bbe5b5b873b16acb84ad718b", "$lat,$lon"
            )
            if (weatherResponse.isSuccessful) {
                weatherResponse.body()?.let {
                    if (localCachedWeatherResponse == null || localCachedWeatherResponse != it) {
                        broadcastWeather(it)
                        cachedWeatherResponse = it
                    }
                }
            } else {
                Log.w("WeatherStack call not successful", "${weatherResponse.code()}")
            }
        }
    }

    private fun broadcastWeather(weatherResponse: IWeather) {
        Intent().also { intent ->
            intent.action = WEATHER_BROADCAST_ID
            intent.putExtra("weather", weatherResponse.description())
            intent.putExtra("windspeed", weatherResponse.windSpeed())
            intent.putExtra("clouds", weatherResponse.cloudsPercentage())
            intent.putExtra("temp", weatherResponse.temperature())
            application.sendBroadcast(intent)
        }
    }
}