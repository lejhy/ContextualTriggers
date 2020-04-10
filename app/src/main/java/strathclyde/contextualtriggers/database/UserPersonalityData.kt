package strathclyde.contextualtriggers.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import strathclyde.contextualtriggers.enums.PersonalityKey

@Entity
data class UserPersonalityData(
    @PrimaryKey
    var id: Long = 0L,
    var value: Int = 0,
    var key: PersonalityKey = PersonalityKey.DEFAULT
)