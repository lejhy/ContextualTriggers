package strathclyde.contextualtriggers.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

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
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'IN_VEHICLE', 1, 1, 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'ON_BICYCLE', 1, 1, 2)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'ON_FOOT', 1, 1, 3)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'RUNNING', 1, 1, 4)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'STILL', 1, 1, 5)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'WALKING', 1, 1, 6)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'SUNNY', 1, 1, 7)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'HAZE', 1, 1, 8)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'RAIN', 1, 1, 9)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'TEMPERATURE', 0, 20, 10)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'BATTERY_LEVEL', 80, 100, 11)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'HEADPHONES', 1, 1, 12)")
                db.execSQL("Insert Into 'ContextConstraint' VALUES (NULL, 'TIME',${100},${711},13)")
                db.execSQL("Insert Into 'ContextConstraint' VALUES (NULL, 'TIME',${600},${711},14)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',10,11,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',110,111,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',210,211,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',310,311,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',410,411,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',510,511,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',610,611,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',710,711,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',10,11,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',110,111,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',210,211,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',310,311,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',410,411,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',510,511,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',610,611,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',710,711,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'STEPS', 50, 99, 17)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'STEPS', 100, 100, 18)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'WIND_SPEED', 0, 10, 19)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'CLOUDS', 0, 20, 20)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'AT_HOME', 1, 1, 21)")
            }
        }
    }
}
