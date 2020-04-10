package strathclyde.contextualtriggers.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface UserPersonalityDataDao {

    @Transaction
    @Query ("SELECT * FROM `UserPersonalityData`")
    fun getAll(): List<UserPersonalityData>

    @Update
    fun updateUserPersonalityData(userPersonalityData: UserPersonalityData)

}