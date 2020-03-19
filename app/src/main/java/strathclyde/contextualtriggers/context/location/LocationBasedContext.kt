package strathclyde.contextualtriggers.context.location;

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.util.Log
import androidx.core.content.ContextCompat
import strathclyde.contextualtriggers.context.Context


abstract class LocationBasedContext(
    private val application: Application
) : Context() {

    abstract fun useLocation(lat: Double, long: Double): Int

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            intent?.let {
                var longitude = 0.0
                var latitude = 0.0
                if (
                    ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val lm = context.getSystemService(LOCATION_SERVICE) as LocationManager?
                    val location: Location =
                        lm?.getLastKnownLocation(GPS_PROVIDER) ?: Location("natural")
                    longitude = location.longitude
                    latitude = location.latitude
                }

                Log.d("LOCATION BASED CONTEXT", "Updated lat: $latitude, long: $longitude")
                update(useLocation(latitude, longitude))
            }
        }
    }

    override fun onStart() {
        application.registerReceiver(
            receiver,
            IntentFilter(Intent.ACTION_TIME_TICK)
        ) //Calls intent every minute

    }

    override fun onStop() {
        application.unregisterReceiver(receiver)

    }
}
