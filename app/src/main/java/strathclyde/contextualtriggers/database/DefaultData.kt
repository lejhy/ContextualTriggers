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
            return listOf(
                Trigger(
                    title = "In Vehicle",
                    content = "You are in vehicle...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "On Bicycle",
                    content = "You are on bicycle...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "On Foot",
                    content = "You are on foot...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Running",
                    content = "You are running...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Still",
                    content = "You are still...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Walking",
                    content = "You are walking...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Sunny",
                    content = "The weather is sunny...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Haze",
                    content = "There is haze outside....",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Rain",
                    content = "Its raining outside...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Warm outside",
                    content = "Its warm outside...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Warm outside",
                    content = "Its warm outside...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Battery good",
                    content = "Battery above 80%'",
                    altContent = "Keep your phone charged!",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Headphones",
                    content = "Headphones are plugged in...",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Day",
                    content = "Day changed",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Weekend",
                    content = "Day changed to a saturday or sunday",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Hour",
                    content = "Hour changed",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Minute",
                    content = "Minute changed",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Half-way done",
                    content = "You have completed half of your steps! Keep going!",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Steps completed",
                    content = "You completed your steps! Good job!",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Wind speed",
                    content = "There is little wind",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
                    title = "Clouds",
                    content = "There is no or little clouds",
                    iconKey = "NOTIFICATION_IMPORTANT",
                    active = true
                ),
                Trigger(
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