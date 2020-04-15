package strathclyde.contextualtriggers.utils

import android.app.Application
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import android.util.Log
import strathclyde.contextualtriggers.broadcasters.HistoryJobService
import strathclyde.contextualtriggers.broadcasters.WeatherJobService
import strathclyde.contextualtriggers.utils.LocationUtils.Companion.getLocation

class JobUtils {
    companion object {
        private const val WEATHER_JOB_ID = 10000
        private const val HISTORY_JOB_ID = 20000
        fun startHistoryJob(application: Application) {
            if (isJobServiceOn(application)) return
            val componentName = ComponentName(application, HistoryJobService::class.java)
            val info = JobInfo.Builder(HISTORY_JOB_ID, componentName)
                .setPersisted(true)
                .setRequiresBatteryNotLow(true)
                .setPeriodic(15)
                .build()
            val scheduler =
                application.getSystemService(Service.JOB_SCHEDULER_SERVICE) as JobScheduler
            try {
                val response = scheduler.schedule(info)
                if (response == JobScheduler.RESULT_SUCCESS) {
                    Log.d("History job scheduled", "SUCCESS")
                } else {
                    Log.d("History job not scheduler", "FAILURE")
                }
            } catch (e: Exception) {
                Log.e("Failed to start history job", "No response", e)
            }
        }
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
                    .setRequiresBatteryNotLow(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING)
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