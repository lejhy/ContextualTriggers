package strathclyde.contextualtriggers.UserPersonality

import android.app.Application
import strathclyde.contextualtriggers.database.MainDatabase
import strathclyde.contextualtriggers.database.UserPersonalityData
import strathclyde.contextualtriggers.enums.PersonalityKey

class UserPersonalityDecider(application: Application) {
    val emotions: List<UserPersonalityData> =
        MainDatabase.getInstance(application.applicationContext).userPersonalityDataDao.getAll()


    fun isPositivePersonality(): Boolean {
        val motivated = emotions.first { e -> e.key == PersonalityKey.MOTIVATED }
        val lazy = emotions.first { e -> e.key == PersonalityKey.LAZY }
        return motivated.value > lazy.value

    }
}