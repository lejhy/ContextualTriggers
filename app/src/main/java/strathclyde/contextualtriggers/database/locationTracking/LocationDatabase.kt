package strathclyde.contextualtriggers.database.locationTracking;

import LocationEntry
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
        entities = [LocationEntry::class],
        version = 1,
        exportSchema = false
)

abstract class LocationDatabase : RoomDatabase(){

}
