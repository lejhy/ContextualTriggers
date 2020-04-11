package strathclyde.contextualtriggers.context.weather

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import org.json.JSONObject
import strathclyde.contextualtriggers.broadcasters.WeatherBroadcast
import strathclyde.contextualtriggers.context.Context

class CloudsContext(
    private val application: Application
) : Context() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            val result = intent?.getStringExtra("result")
            if (result != null) {
                val jsonObj = JSONObject(result)
                val clouds = jsonObj.getJSONObject("clouds")
                update(clouds.getInt("all"))
                Log.d("CLOUDS CONTEXT", "Updated ${clouds.getInt("all")}")
            }
        }
    }

    override fun onStart() {
        application.registerReceiver(receiver, IntentFilter(WeatherBroadcast.WEATHER_BROADCAST_ID))
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
    }
}