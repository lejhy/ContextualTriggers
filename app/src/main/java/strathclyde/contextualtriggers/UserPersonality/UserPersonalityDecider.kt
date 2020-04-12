package strathclyde.contextualtriggers.UserPersonality

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
    val dbSave: DBSave = DBSave()
    val dbLoad: DBLoad = DBLoad()

    var emotions: List<UserPersonalityData>? = null

    init {
        dbLoad.execute(this)
    }

    fun isPositivePersonality(): Boolean {
        val emotional = emotions
        if (emotional != null) {
            val motivated = emotional.first { e -> e.key == PersonalityKey.MOTIVATED.resolve() }
            val lazy = emotional.first { e -> e.key == PersonalityKey.LAZY.resolve() }
            return motivated.value > lazy.value
        }
        return true
    }

    fun updatePersonality(key: Int, value: Int) {
        val emotional = emotions
        if (emotional != null) {
            val personality = emotional.first { e -> e.key == key }
            personality.value = value
            dbSave.execute(this)
        }

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


    class DBSave : AsyncTask<UserPersonalityDecider, Void, Void?>() {
        override fun doInBackground(vararg p0: UserPersonalityDecider?): Void? {
            val master = p0.first()
            master?.emotions?.forEach { master.userPersonalityDataDao.update(it) }
            Log.d("AT HOME CONTEXT", "user-personality data saved" + (master?.emotions))
            return null
        }

    }

    class DBLoad : AsyncTask<UserPersonalityDecider, Void, Void?>() {
        override fun doInBackground(vararg p0: UserPersonalityDecider?): Void? {
            val master = p0.first()

            if (master != null)
                master.emotions = master.userPersonalityDataDao.getAll()
            Log.d("AT HOME CONTEXT", "user-personality data loaded" + (master?.emotions))
            return null
        }

    }
}