package strathclyde.contextualtriggers.userPersonality

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import strathclyde.contextualtriggers.database.MainDatabase
import strathclyde.contextualtriggers.database.UserPersonalityData
import strathclyde.contextualtriggers.database.UserPersonalityDataDao
import strathclyde.contextualtriggers.enums.PersonalityKey

class UserPersonalityDecider(context: Context) {
    val userPersonalityDataDao: UserPersonalityDataDao =
        MainDatabase.getInstance(context).userPersonalityDataDao

    var emotions: MutableList<UserPersonalityData>? = null

    init {
        DBLoad().execute(this)
    }

    fun isPositivePersonality(): Boolean {
        val emotional = emotions
        Log.d("PERSONALITY", "Testing: " + (emotional == null).toString())
        if (emotional != null && emotional.isNotEmpty())
            return emotional.first { e -> e.key == PersonalityKey.MOTIVATED.resolve() }.value >= 0
        return true
    }

    fun updatePersonality(key: Int, value: Int) {
        val emotional = emotions
        if (emotional != null) {
            try {
                val personality = emotional.first { e -> e.key == key }
                personality.value = value
            } catch (e: NoSuchElementException) {
                emotional.add(UserPersonalityData(key, value))
            }
            DBSave().execute(this)
        }

    }


    companion object {
        @Volatile
        private var deciderInstance: UserPersonalityDecider? = null

        fun getDecider(context: Context): UserPersonalityDecider {
            synchronized(this) {
                var instance = deciderInstance
                return if (instance == null) {
                    Log.d("PERSONALITY", "New instance")
                    deciderInstance = UserPersonalityDecider(context)
                    instance = deciderInstance
                    instance!!
                } else
                    instance
            }
        }
    }


    class DBSave : AsyncTask<UserPersonalityDecider, Void, Void?>() {
        override fun doInBackground(vararg p0: UserPersonalityDecider?): Void? {
            val master = p0.first()
            master?.emotions?.forEach { master.userPersonalityDataDao.update(it) }
            Log.d("PERSONALITY", "user-personality data saved${master?.emotions}")
            return null
        }

    }

    class DBLoad : AsyncTask<UserPersonalityDecider, Void, Void?>() {
        override fun doInBackground(vararg p0: UserPersonalityDecider?): Void? {
            val master = p0.first()
            if (master != null) master.emotions = master.userPersonalityDataDao.getAll().toMutableList()
            Log.d("PERSONALITY", "user-personality data loaded${master?.emotions}")
            return null
        }

    }
}