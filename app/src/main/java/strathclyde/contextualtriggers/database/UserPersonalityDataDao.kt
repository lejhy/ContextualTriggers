package strathclyde.contextualtriggers.database

import androidx.room.*

@Dao
interface UserPersonalityDataDao {

    @Transaction
    @Query("SELECT * FROM `UserPersonalityData`")
    fun getAll(): List<UserPersonalityData>

    @Update
    fun update(userPersonalityData: UserPersonalityData)

    @Insert
    fun insertPersonalities(personalityData: List<UserPersonalityData>)

}