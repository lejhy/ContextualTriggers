package strathclyde.contextualtriggers.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class LocationEntryDao {
    @Transaction
    @Query("SELECT * FROM 'LocationEntry'")
    abstract fun getAll(): MutableList<LocationEntry>

}