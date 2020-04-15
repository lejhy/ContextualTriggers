package strathclyde.contextualtriggers.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat

class LocationUtils {
    companion object {
        fun getLocation(context: Context): Location? {
            if (ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val lm =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
                return lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            Log.d("Location Null", "null returned")
            return null
        }
    }
}