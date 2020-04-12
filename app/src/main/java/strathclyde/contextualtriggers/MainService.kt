package strathclyde.contextualtriggers

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import strathclyde.contextualtriggers.broadcasters.WeatherBroadcast
import strathclyde.contextualtriggers.context.*
import strathclyde.contextualtriggers.context.activity.*
import strathclyde.contextualtriggers.context.battery.BatteryLevelContext
import strathclyde.contextualtriggers.context.location.AtHouseContext
import strathclyde.contextualtriggers.context.headphones.HeadphonesContext
import strathclyde.contextualtriggers.context.steps.BasicStepsContext
import strathclyde.contextualtriggers.context.time.TimeContext
import strathclyde.contextualtriggers.context.weather.*
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
        WeatherBroadcast(application).also { broadcast -> broadcast.start() } //TODO Make it context aware
        val database = MainDatabase.getInstance(this)
        triggerWithContextConstraintsDao = database
            .triggerWithContextConstraintsDao

        initializeContexts()
        initializeTriggers()

        Log.d("SET_UP FIRST TIME", triggers.isEmpty().toString())
        if(triggers.isEmpty()) {
            setUpDefaultData(database)
            initializeTriggers()
        }
    }

    private fun setUpDefaultData(database: MainDatabase) {
        val triggers = setUpDefaultTriggers()
        val emotions = setUpDefaultEmotion()
        DBSave().execute(database, triggers, emotions)
    }

    private fun setUpDefaultEmotion(): List<UserPersonalityData> {
        return listOf(
            UserPersonalityData(
                key = PersonalityKey.LAZY.resolve(),
                value = 1
            ),
            UserPersonalityData(
                key = PersonalityKey.MOTIVATED.resolve(),
                value = 0
            ),
            UserPersonalityData(
                key = PersonalityKey.DEFAULT.resolve(),
                value = 0
            )
        )
    }

    private fun setUpDefaultTriggers() : List<strathclyde.contextualtriggers.database.Trigger>{
        return listOf(
            strathclyde.contextualtriggers.database.Trigger(
                title = "In Vehicle",
                content = "You are in vehicle...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "On Bicycle",
                content = "You are on bicycle...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "On Foot",
                content = "You are on foot...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Running",
                content = "You are running...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Still",
                content = "You are still...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Walking",
                content = "You are walking...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Sunny",
                content = "The weather is sunny...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Haze",
                content = "There is haze outside....",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Rain",
                content = "Its raining outside...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Warm outside",
                content = "Its warm outside...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Warm outside",
                content = "Its warm outside...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Battery good",
                content = "Battery above 80%'",
                altContent = "Keep your phone charged!",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Headphones",
                content = "Headphones are plugged in...",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Day",
                content = "Day changed",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Weekend",
                content = "Day changed to a saturday or sunday",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Hour",
                content = "Hour changed",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Minute",
                content = "Minute changed",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Half-way done",
                content = "You have completed half of your steps! Keep going!",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Steps completed",
                content = "You completed your steps! Good job!",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Wind speed",
                content = "There is little wind",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "Clouds",
                content = "There is no or little clouds",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            ),
            strathclyde.contextualtriggers.database.Trigger(
                title = "At Home",
                content = "UwU u is at home",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true
            )
        )
    }

    class DBSave : AsyncTask<Any, Void, Void?>() {
        override fun doInBackground(vararg p0: Any?): Void? {

            val possibleDatabase = p0[0]
            val possibleTriggerArray = p0[1]
            val possibleEmotionArray = p0[2]
            if (possibleDatabase is MainDatabase && possibleTriggerArray is List<*> && possibleEmotionArray is List<*>) {
                possibleDatabase.triggerWithContextConstraintsDao.insertTriggers(
                    possibleTriggerArray as List<strathclyde.contextualtriggers.database.Trigger>
                )
                possibleDatabase.userPersonalityDataDao.insertPersonalities(
                    possibleEmotionArray as List<UserPersonalityData>
                )
                Log.d("SET_UP TRIGGERS",
                    possibleDatabase.triggerWithContextConstraintsDao.getAll().toString()
                )
                Log.d("SET_UP EMOTIONS",
                    possibleDatabase.userPersonalityDataDao.getAll().toString()
                )
            }
            return null
        }
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
