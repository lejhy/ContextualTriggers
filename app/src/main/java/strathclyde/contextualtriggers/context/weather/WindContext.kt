package strathclyde.contextualtriggers.context.weather

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import strathclyde.contextualtriggers.broadcasters.WeatherJobService.Companion.WEATHER_BROADCAST_ID
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.utils.JobUtils
import kotlin.math.roundToInt

class WindContext(
    private val application: Application
) : Context() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            val windSpeed = intent?.getFloatExtra("windspeed", 0f)
            if (windSpeed != null) {
                update(windSpeed.roundToInt())
                Log.d("WIND CONTEXT", "Updated ${windSpeed.roundToInt()}")
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