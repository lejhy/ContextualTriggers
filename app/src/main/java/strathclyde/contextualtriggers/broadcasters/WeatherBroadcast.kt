package strathclyde.contextualtriggers.broadcasters

import android.app.Application
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.google.android.gms.location.LocationServices
import java.net.URL

class WeatherBroadcast(
    private val application: Application
) {
    companion object {
        const val WEATHER_BROADCAST_ID = "strathclyde.contextualtriggers.broadcast.WEATHER"
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private var lat: Double? = null
    private var lon: Double? = null
    private var client = LocationServices.getFusedLocationProviderClient(application)

    fun start() {
        client.lastLocation.addOnSuccessListener {
            mainHandler.removeCallbacks(updateWeather)
            lat = it.latitude
            lon = it.longitude //TODO Deal with what if no Location
            updateWeather.run()
        }
    }

    fun stop() {
        mainHandler.removeCallbacks(updateWeather)
    }

    private val updateWeather = object : Runnable {
        override fun run() {
            weatherTask().execute()
            mainHandler.postDelayed(this, 60000)
        }
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {
            return try {
                URL("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=2c6c4f6e4567938518b066d7122660d1").readText(
                    Charsets.UTF_8
                )
            } catch (e: Exception) {
                null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Intent().also { intent ->
                intent.action = WEATHER_BROADCAST_ID
                intent.putExtra("result", result)
                application.sendBroadcast(intent)
            }
        }
    }
}