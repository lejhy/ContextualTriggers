package strathclyde.contextualtriggers.context.location

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.utils.LocationUtils.Companion.getLocation


abstract class LocationBasedContext(
    protected val application: Application
) : Context() {

    abstract fun useLocation(lat: Double, long: Double): Int

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            intent?.let {
                Log.d("LOCATION BASED CONTEXT", "location-based context called")
                val location = getLocation(context)
                if (location != null) {
                    update(useLocation(location.latitude, location.longitude))
                    Log.d(
                        "LOCATION BASED CONTEXT",
                        "Updated lat: ${location.latitude}, long: ${location.longitude}"
                    )
                }
            }
        }
    }

    override fun onStart() {
        application.registerReceiver(
            receiver,
            IntentFilter(Intent.ACTION_TIME_TICK)
        ) //Calls intent every minute
        Log.d("LOCATION BASED CONTEXT", "location-based context started")

    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
        Log.d("LOCATION BASED CONTEXT", "location-based context stopped")

    }
}
