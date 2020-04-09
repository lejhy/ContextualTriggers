package strathclyde.contextualtriggers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Trigger::class, ContextConstraint::class],
    version = 4,
    exportSchema = false
)

abstract class MainDatabase : RoomDatabase() {
    abstract val triggerWithContextConstraintsDao: TriggerWithContextConstraintsDao

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
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'In Vehicle', 'You are in vehicle...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'IN_VEHICLE', 1, 1, 1)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'On Bicycle', 'You are on bicycle...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'ON_BICYCLE', 1, 1, 2)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'On Foot', 'You are on foot...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'ON_FOOT', 1, 1, 3)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Running', 'You are running...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'RUNNING', 1, 1, 4)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Still', 'You are still...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'STILL', 1, 1, 5)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Walking', 'You are walking...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'WALKING', 1, 1, 6)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Sunny', 'The weather is sunny...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'SUNNY', 1, 1, 7)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Haze', 'There is haze outside...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'HAZE', 1, 1, 8)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Rain', 'Its raining outside...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'RAIN', 1, 1, 9)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Warm outside', 'Its warm outside...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'TEMPERATURE', 0, 20, 10)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Battery good', 'Battery above 80%', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'BATTERY_LEVEL', 80, 100, 11)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Headphones', 'Headphones are plugged in...', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'HEADPHONES', 1, 1, 12)")
                db.execSQL("Insert Into 'Trigger' VALUES (NULL, 'Day', 'Day changed', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("Insert Into 'ContextConstraint' VALUES (NULL, 'TIME',${100},${711},13)")
                db.execSQL("Insert Into 'Trigger' VALUES (NULL, 'Weekend', 'Day changed to a saturday or sunday', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("Insert Into 'ContextConstraint' VALUES (NULL, 'TIME',${600},${711},14)")
                db.execSQL("Insert Into 'Trigger' VALUES(NULL, 'Hour', 'Hour changed', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',10,11,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',110,111,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',210,211,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',310,311,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',410,411,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',510,511,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',610,611,15)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',710,711,15)")
                db.execSQL("Insert Into 'Trigger' VALUES(NULL, 'Minute', 'Minute changed', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',10,11,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',110,111,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',210,211,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',310,311,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',410,411,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',510,511,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',610,611,16)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES(NULL, 'TIME',710,711,16)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Half-way done', 'You have completed half of your steps! Keep going!', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'STEPS', 50, 99, 17)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Steps completed', 'You completed your steps! Good job!', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'STEPS', 100, 100, 18)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Wind speed', 'There is little wind', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'WIND_SPEED', 0, 10, 19)")
                db.execSQL("INSERT INTO 'Trigger' VALUES (NULL, 'Clouds', 'There is no or little clouds', 'NOTIFICATION_IMPORTANT', 1)")
                db.execSQL("INSERT INTO 'ContextConstraint' VALUES (NULL, 'CLOUDS', 0, 20, 20)")
            }
        }
    }
}
