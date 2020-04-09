package strathclyde.contextualtriggers.database

import androidx.room.*

@Dao
interface LocationEntryDao {
    @Transaction
    @Query("SELECT * FROM 'LocationEntry'")
    abstract fun getAll(): MutableList<LocationEntry>

    @Update
    fun update(vararg entry : LocationEntry)
}