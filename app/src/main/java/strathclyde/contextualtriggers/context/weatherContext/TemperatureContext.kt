package strathclyde.contextualtriggers.context.weatherContext

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import org.json.JSONObject
import strathclyde.contextualtriggers.broadcasters.WeatherBroadcast
import strathclyde.contextualtriggers.context.Context

class TemperatureContext(
    private val application: Application
) : Context() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
//            Log.d("(TEMPERATURE)Got Weather ", intent?.getStringExtra("result")!!)
            val result = intent?.getStringExtra("result")
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            update(main.getInt("temp"))
        }
    }

    override fun onStart() {
        application.registerReceiver(receiver, IntentFilter(WeatherBroadcast.WEATHER_BROADCAST_ID))
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
    }
}
