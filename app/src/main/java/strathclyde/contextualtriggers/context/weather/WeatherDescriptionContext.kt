package strathclyde.contextualtriggers.context.weather

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import strathclyde.contextualtriggers.broadcasters.WeatherJobService.Companion.WEATHER_BROADCAST_ID
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.utils.JobUtils

abstract class WeatherDescriptionContext(
    private val application: Application,
    private val currentWeather: String

) : Context() {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            val weather = intent?.getStringExtra("weather")
            if (weather != null) {
                if (weather.toUpperCase() == currentWeather) {
                    update(1)
                } else {
                    update(0)
                }
                Log.d("WEATHER DESCRIPTION CONTEXT $weather", "Updated")
            }
        }
    }

    override fun onStart() {
        application.registerReceiver(receiver, IntentFilter(WEATHER_BROADCAST_ID))
        JobUtils.startWeatherJob(application)
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
    }

}