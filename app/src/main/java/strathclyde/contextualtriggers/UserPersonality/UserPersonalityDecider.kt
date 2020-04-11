package strathclyde.contextualtriggers.UserPersonality

import android.app.Application
import android.content.Context
import strathclyde.contextualtriggers.database.MainDatabase
import strathclyde.contextualtriggers.database.UserPersonalityData
import strathclyde.contextualtriggers.enums.PersonalityKey

class UserPersonalityDecider(context: Context) {
    val emotions: List<UserPersonalityData> =
        MainDatabase.getInstance(context).userPersonalityDataDao.getAll()

    fun isPositivePersonality(): Boolean {
        val motivated = emotions.first { e -> e.key == PersonalityKey.MOTIVATED.resolve() }
        val lazy = emotions.first { e -> e.key == PersonalityKey.LAZY.resolve() }
        return motivated.value > lazy.value

    }


    companion object {
        @Volatile
        private var deciderInstance: UserPersonalityDecider? = null

        fun getDecider(context: Context): UserPersonalityDecider {
            synchronized(this) {
                var instance = deciderInstance
                return if (instance == null) {
                    deciderInstance = UserPersonalityDecider(context)
                    instance = deciderInstance
                    instance!!
                } else {
                    instance
                }
            }
        }
    }
}