package strathclyde.contextualtriggers

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import strathclyde.contextualtriggers.UserPersonality.UserPersonalityDecider
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.context.activity.*
import strathclyde.contextualtriggers.context.battery.BatteryLevelContext
import strathclyde.contextualtriggers.context.location.AtHouseContext
import strathclyde.contextualtriggers.context.headphones.HeadphonesContext
import strathclyde.contextualtriggers.context.steps.BasicStepsContext
import strathclyde.contextualtriggers.context.time.TimeContext
import strathclyde.contextualtriggers.context.weather.*
import strathclyde.contextualtriggers.database.DefaultData
import strathclyde.contextualtriggers.database.MainDatabase
import strathclyde.contextualtriggers.database.TriggerWithContextConstraintsDao
import strathclyde.contextualtriggers.database.UserPersonalityData
import strathclyde.contextualtriggers.enums.PersonalityKey
import strathclyde.contextualtriggers.trigger.Trigger
import java.util.*

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
        val database = MainDatabase.getInstance(this)
        triggerWithContextConstraintsDao = database
            .triggerWithContextConstraintsDao
        initializeContexts()
        initializeTriggers(database)
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
                WindContext(application),
                CloudsContext(application),
                BatteryLevelContext(application),
                HeadphonesContext(application),
                TimeContext(application),
                BasicStepsContext(application),
                AtHouseContext(application)
            )
        )
    }

    private fun initializeTriggers(database: MainDatabase) {
        val temp = this
        scope.launch {
            var isFirstLaunch = true
            val triggerWithContextConstraints = withContext(Dispatchers.IO) {
                triggerWithContextConstraintsDao.getAll()
            }
            triggerWithContextConstraints.forEach {
                val trigger = Trigger(application, contexts, it)
                isFirstLaunch = false
                triggers.add(trigger)
            }
            if(isFirstLaunch) {
                DefaultData.setUpDefaultData(database)
                initializeTriggers(database)
            }
            UserPersonalityDecider.getDecider(temp)
        }
    }
}
