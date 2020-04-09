package strathclyde.contextualtriggers.context.weather

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import org.json.JSONObject
import strathclyde.contextualtriggers.broadcasters.WeatherBroadcast.Companion.WEATHER_BROADCAST_ID
import strathclyde.contextualtriggers.context.Context

abstract class WeatherDescriptionContext(
    private val application: Application,
    private val currentWeather: String

) : Context() {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            val result = intent?.getStringExtra("result")
            if (result != null) {
                val jsonObj = JSONObject(result)
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val weatherMain = weather.getString("main")
                if (weatherMain.toUpperCase() == currentWeather) {
                    update(1)
                } else {
                    update(0)
                }
                Log.d("WEATHER DESCRIPTION CONTEXT $weatherMain", "Updated")
            }
        }
    }

    override fun onStart() {
        application.registerReceiver(receiver, IntentFilter(WEATHER_BROADCAST_ID))
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
    }

}