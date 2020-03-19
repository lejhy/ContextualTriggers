package strathclyde.contextualtriggers

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import strathclyde.contextualtriggers.broadcasters.WeatherBroadcast
import strathclyde.contextualtriggers.context.*
import strathclyde.contextualtriggers.context.battery.BatteryLevelContext
import strathclyde.contextualtriggers.context.location.AtHouseContext
import strathclyde.contextualtriggers.context.weather.HazeContext
import strathclyde.contextualtriggers.context.weather.RainContext
import strathclyde.contextualtriggers.context.weather.SunnyContext
import strathclyde.contextualtriggers.context.weather.TemperatureContext
import strathclyde.contextualtriggers.database.MainDatabase
import strathclyde.contextualtriggers.database.TriggerWithContextConstraintsDao
import strathclyde.contextualtriggers.trigger.Trigger

class MainService : Service() {

    private val contexts: MutableList<Context> = mutableListOf()
    private val triggers: MutableList<Trigger> = mutableListOf()

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var triggerWithContextConstraintsDao: TriggerWithContextConstraintsDao

    override fun onCreate() {
        super.onCreate()
        Log.i("MainService", "onCreate called")
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(this, getString(R.string.channel_id))
            .setContentTitle(getString(R.string.contextualTriggers))
            .setContentText(getString(R.string.ContextualTriggersAreNowRunning))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification_important_black_24dp)
            .build()

        startForeground(R.integer.contextualTriggersNotificationId, notification)
        WeatherBroadcast(application).also { broadcast -> broadcast.start() } //TODO Make it context aware

        triggerWithContextConstraintsDao =
            MainDatabase.getInstance(this).triggerWithContextConstraintsDao
        initializeContexts()
        initializeTriggers()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.i("MainService", "onDestroy called")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun initializeContexts() {
        contexts.addAll(
            listOf(
                InVehicleContext(application),
                OnBicycleContext(application),
                OnFootContext(application),
                RunningContext(application),
                StillContext(application),
                WalkingContext(application),
                SunnyContext(application),
                HazeContext(application),
                RainContext(application),
                TemperatureContext(application),
                BatteryLevelContext(application),
                AtHouseContext(application)
            )
        )
    }

    private fun initializeTriggers() {
        scope.launch {
            val triggerWithContextConstraints = withContext(Dispatchers.IO) {
                triggerWithContextConstraintsDao.getAll()
            }
            triggerWithContextConstraints.forEach {
                val trigger = Trigger(application, contexts, it)
                triggers.add(trigger)
            }
        }
    }
}
