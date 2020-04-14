package strathclyde.contextualtriggers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Trigger::class, ContextConstraint::class, LocationEntry::class, UserPersonalityData::class],
    version = 7,
    exportSchema = false
)

abstract class MainDatabase : RoomDatabase() {
    abstract val triggerWithContextConstraintsDao: TriggerWithContextConstraintsDao
    abstract val userPersonalityDataDao: UserPersonalityDataDao
    abstract val locationEntryDao: LocationEntryDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(context: Context): MainDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "main_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(CALLBACK)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            fun createConstraint(
                db: SupportSQLiteDatabase,
                context: String,
                triggerId: Int,
                atLeast: Int = 1,
                atMost: Int = 1
            ) {
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, '$context', $atLeast, $atMost, $triggerId)")
            }

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                var triggerId = 1
                createConstraint(db, "IN_VEHICLE", triggerId++)
                createConstraint(db, "ON_BICYCLE", triggerId++)
                createConstraint(db, "ON_FOOT", triggerId++)
                createConstraint(db, "RUNNING", triggerId++)
                createConstraint(db, "STILL", triggerId++)
                createConstraint(db, "WALKING", triggerId++)
                createConstraint(db, "SUNNY", triggerId++)
                createConstraint(db, "HAZE", triggerId++)
                createConstraint(db, "RAIN", triggerId++)
                createConstraint(db, "TEMPERATURE", triggerId++, 0, 20)
                createConstraint(db, "BATTERY_LEVEL", triggerId++, 80, 100)
                createConstraint(db, "HEADPHONES", triggerId++)

                //day
                for (day in (1..7))
                    createConstraint(db, "TIME", triggerId, day * 10000 + 2359, day * 10000 + 2359)
                triggerId++

                //weekend
                createConstraint(db, "TIME", triggerId++, 60000, 72359) //Is weekend

                //morning(07;00 -> 11:59)
                for (day in (1..7))
                    createConstraint(db, "TIME", triggerId, day * 10000 + 700, day * 10000 + 1159)
                triggerId++

                // not evening (07:00 ->18:59)
                for (day in (1..7))
                    createConstraint(db, "TIME", triggerId, day * 10000 + 700, day * 10000 + 1859)
                triggerId++

                createConstraint(db, "STEPS", triggerId++, 99, 17)
                createConstraint(db, "STEPS", triggerId++, 100, 100)
                createConstraint(db, "WIND_SPEED", triggerId++, 0, 10)
                createConstraint(db, "CLOUDS", triggerId++, 0, 20)
                createConstraint(db, "AT_HOME", triggerId++, 1, 1)

            }
        }
    }
}
