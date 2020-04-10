package strathclyde.contextualtriggers.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import strathclyde.contextualtriggers.enums.PersonalityKey

@Entity
data class UserPersonalityData(
    @PrimaryKey
    var key: PersonalityKey = PersonalityKey.DEFAULT,
    var value: Int = 0
)