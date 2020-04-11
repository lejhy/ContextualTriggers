package strathclyde.contextualtriggers.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import strathclyde.contextualtriggers.enums.PersonalityKey

@Entity
data class UserPersonalityData(
    @PrimaryKey
    var key: Int = PersonalityKey.DEFAULT.resolve(),
    var value: Int = 0
)