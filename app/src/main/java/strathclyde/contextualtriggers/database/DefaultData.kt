package strathclyde.contextualtriggers.database

import android.os.AsyncTask
import android.util.Log
import strathclyde.contextualtriggers.enums.PersonalityKey

class DefaultData {
    companion object {
        fun setUpDefaultData(database: MainDatabase) {
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

        private fun setUpDefaultTriggers(): List<Trigger> {
            return listOf( // TEST TRIGGERS
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "In Vehicle",
//                    content = "You are in vehicle...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "On Bicycle",
//                    content = "You are on bicycle...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "On Foot",
//                    content = "You are on foot...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Running",
//                    content = "You are running...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Still",
//                    content = "You are still...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Walking",
//                    content = "You are walking...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Sunny",
//                    content = "The weather is sunny...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Haze",
//                    content = "There is haze outside....",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Rain",
//                    content = "Its raining outside...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Warm outside",
//                    content = "Its warm outside...",
//                    altContent = "Its warm outside, get walking!",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Battery good",
//                    content = "Battery above 80%'",
//                    altContent = "Keep your phone charged!",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Headphones",
//                    content = "Headphones are plugged in...",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Day",
//                    content = "Day changed",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Weekend",
//                    content = "Day changed to a saturday or sunday",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ), Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Morning",
//                    content = "Is morning",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ), Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Not Evening",
//                    content = "Is not evening",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Wind speed",
//                    content = "There is little wind",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "Clouds",
//                    content = "There is no or little clouds",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                ),
//                Trigger(
//                    owner = "strathclyde.contextualtriggers",
//                    title = "At Home",
//                    content = "You are currently at home.",
//                    iconKey = "NOTIFICATION_IMPORTANT",
//                    active = true,
//                    useProgressBar = false,
//                    actionKeys = "",
//                    progressContentUri = "",
//                    actionContentUri = ""
//                )
//
// ACTUAL TRIGGERS
                Trigger(
                    owner = "strathclyde.contextualtriggers",
                    title = "Morning Weekend Walk",
                    content = "It is a weekend morning with sunny weather. Why don't you go for a walk?",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true,
                    useProgressBar = false,
                    actionKeys = "",
                    progressContentUri = "",
                    actionContentUri = ""
                ),
                Trigger(
                    owner = "strathclyde.contextualtriggers",
                    title = "Walk with Headphones in Nice Weather",
                    content = "The weather looks nice you should go for a walk and listen to some music!",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true,
                    useProgressBar = false,
                    actionKeys = "",
                    progressContentUri = "",
                    actionContentUri = ""
                ),
                Trigger(
                    owner = "strathclyde.contextualtriggers",
                    title = "Weekly Summary",
                    content = "Your weekly steps summary is ...!", // add the steps
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true,
                    useProgressBar = false,
                    actionKeys = "",
                    progressContentUri = "",
                    actionContentUri = ""
                ), // The next two triggers are complementing each other
                Trigger(
                    owner = "strathclyde.contextualtriggers",
                title = "Half-way done",
                content = "You have completed half of your steps! Keep going!",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true,
                useProgressBar = true,
                actionKeys = "",
                progressContentUri = "app.uri",
                actionContentUri = ""
            ),
            Trigger(
                owner = "strathclyde.contextualtriggers",
                title = "Steps completed",
                content = "You completed your steps! Good job!",
                iconKey = "NOTIFICATION_IMPORTANT",
                active = true,
                useProgressBar = true,
                actionKeys = "",
                progressContentUri = "app.uri",
                actionContentUri = ""
            ),Trigger(
                    owner = "strathclyde.contextualtriggers",
                    title = "Adjusting Steps Notification",
                    content = "You almost completed a bigger goal do you want to switch to it!",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true,
                    useProgressBar = false,
                    actionKeys = "bigger_goal",
                    progressContentUri = "",
                    actionContentUri = "app.uri"
                )

            )
        }

        @Suppress("UNCHECKED_CAST")
        class DBSave : AsyncTask<Any, Void, Void?>() {
            override fun doInBackground(vararg p0: Any?): Void? {

                val possibleDatabase = p0[0]
                val possibleTriggerArray = p0[1]
                val possibleEmotionArray = p0[2]
                if (possibleDatabase is MainDatabase && possibleTriggerArray is List<*> && possibleEmotionArray is List<*>) {
                    possibleDatabase.triggerWithContextConstraintsDao.insertTriggers(
                        possibleTriggerArray as List<Trigger>
                    )
                    possibleDatabase.userPersonalityDataDao.insertPersonalities(
                        possibleEmotionArray as List<UserPersonalityData>
                    )
                    Log.d(
                        "SET_UP TRIGGERS",
                        possibleDatabase.triggerWithContextConstraintsDao.getAll().toString()
                    )
                    Log.d(
                        "SET_UP EMOTIONS",
                        possibleDatabase.userPersonalityDataDao.getAll().toString()
                    )
                }
                return null
            }
        }
    }
}