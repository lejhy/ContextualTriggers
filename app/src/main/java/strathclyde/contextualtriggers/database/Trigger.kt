package strathclyde.contextualtriggers.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import strathclyde.contextualtriggers.enums.PersonalityKey

@Entity
data class Trigger(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var title: String = "",
    var content: String = "",
    var altContent: String = "",
    var iconKey: String = "",
    var active: Boolean = false,
    var success: Int = PersonalityKey.MOTIVATED.resolve(),
    var failure: Int = PersonalityKey.LAZY.resolve()
)
