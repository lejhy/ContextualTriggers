package strathclyde.contextualtriggers.utils

import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.PersistableBundle
import android.util.Log
import androidx.core.content.ContextCompat
import strathclyde.contextualtriggers.broadcasters.WeatherJobService

class JobUtils {
    companion object {
        private const val WEATHER_JOB_ID = 10000

        fun startWeatherJob(context: Context) {
            if (isJobServiceOn(context)) return
            val location = getLocation(context)
            if (location != null) {
                val bundle = PersistableBundle()
                bundle.putDouble("lat", location.latitude)
                bundle.putDouble("lon", location.longitude)
                val componentName = ComponentName(context, WeatherJobService::class.java)
                val info = JobInfo.Builder(WEATHER_JOB_ID, componentName)
                    .setPersisted(true)
                    .setExtras(bundle)
                    .setPeriodic(15 * 60 * 1000) // Cant be set less than 15 min
                    .build()
                val scheduler =
                    context.getSystemService(Service.JOB_SCHEDULER_SERVICE) as JobScheduler
                val response = scheduler.schedule(info)
                if (response == JobScheduler.RESULT_SUCCESS) {
                    Log.d("Weather Job scheduled", "SUCCESS")
                } else {
                    Log.d("Weather Job not scheduled", "FAIL")
                }
            }
        }

        private fun getLocation(context: Context): Location? {
            if (ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val lm =
                    context.getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager?
                Log.d("laction what? ", "what")
                if (lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null) {
                    Log.d("LOCATION LULLL", "NULL")
                    return null
                }
                Log.d("LOCATION LULLL }", "not NULL")
                return lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            Log.d("Location Null", "null")
            return null
        }

        private fun isJobServiceOn(context: Context): Boolean {
            val scheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            var hasBeenScheduled = false
            for (jobInfo in scheduler.allPendingJobs) {
                if (jobInfo.id == WEATHER_JOB_ID) {
                    hasBeenScheduled = true
                    break
                }
            }
            return hasBeenScheduled
        }

    }
}