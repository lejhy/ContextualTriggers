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
                var triggerId = 0

                //region Testing Triggers
                /**
                createConstraint(db, "IN_VEHICLE", triggerId++)
                createConstraint(db, "ON_BICYCLE", triggerId++)
                createConstraint(db, "ON_FOOT", triggerId++)
                createConstraint(db, "RUNNING", triggerId++)
                createConstraint(db, "WALKING", triggerId++)
                createConstraint(db, "HAZE", triggerId++)
                createConstraint(db, "RAIN", triggerId++)
                createConstraint(db, "BATTERY_LEVEL", triggerId++, 80, 100)
                createConstraint(db, "HEADPHONES", triggerId++)
                //day
                for (day in (1..7))
                createConstraint(db, "TIME", triggerId, day * 10000 + 2359, day * 10000 + 2359)
                triggerId++
                createConstraint(db, "WIND_SPEED", triggerId++, 0, 10)
                createConstraint(db, "CLOUDS", triggerId++, 0, 20)

                 **/
                //endregion

                //region Morning Weekend Walk
                //createConstraint(db, "TIME", triggerId, 60000, 72359)// weekend
                for (day in (6..7)) createConstraint(
                    db,
                    "TIME",
                    triggerId,
                    day * 10000 + 700,
                    day * 10000 + 1159
                )
                triggerId++
                createConstraint(db, "AT_HOME", triggerId, 1, 1)
                createConstraint(db, "STILL", triggerId)
                createConstraint(db, "SUNNY", triggerId)
                createConstraint(db, "TEMPERATURE", triggerId, 18, 30)
                triggerId++
                //endregion

                // region Walk with headphones in nice weather
                createConstraint(db, "HEADPHONES", triggerId)
                createConstraint(db, "STILL", triggerId)
                createConstraint(db, "SUNNY", triggerId)
                for (day in (1..7)) createConstraint(
                    db,
                    "TIME",
                    triggerId,
                    day * 10000 + 700,
                    day * 10000 + 1859
                )

                createConstraint(db, "SUNNY", triggerId)
                createConstraint(db, "TEMPERATURE", triggerId, 18, 30)
                triggerId++
                //endregion

                //region Weekly steps summary
                createConstraint(db, "TIME", triggerId, 71800, 72359)
                //TODO check effects of re-enabling:
                // createConstraint(db, "WEEK_COMPLETION",triggerId, 1,100000)
                triggerId++
                //endregion

                //region 50% daily goal completion
                createConstraint(db, "DAY_COMPLETION", triggerId, 50, 99)
                triggerId++
                //endregion

                //region 100% daily goal completion
                createConstraint(db, "DAY_COMPLETION", triggerId, 100, 100)
                triggerId++
                //endregion

                //region Adjusting goal notification
                createConstraint(db, "DAY_COMPLETION", triggerId, 100, 1000000)
                createConstraint(db, "NEXT_GOAL_COMPLETION", triggerId, 30, 10000000)
                triggerId++
                //endregion
//                //NIK TEST CONSTRAINTS
//                createConstraint(db, "CLOUDS", triggerId, 0, 100)
//                createConstraint(db, "TEMPERATURE", triggerId, 0, 100)
//                createConstraint(db, "RAIN", triggerId, 0, 100)
//                createConstraint(db, "WIND_SPEED", triggerId, 0, 100)
//                triggerId++
            }
        }
    }
}
