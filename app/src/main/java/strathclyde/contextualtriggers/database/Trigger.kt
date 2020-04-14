package strathclyde.contextualtriggers.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import strathclyde.contextualtriggers.enums.PersonalityKey

@Entity
data class Trigger(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var owner: String = "",
    var title: String = "",
    var content: String = "",
    var iconKey: String = "",
    var active: Boolean = false,
    var altContent: String = "",
    var success: Int = PersonalityKey.MOTIVATED.resolve(),
    var failure: Int = PersonalityKey.LAZY.resolve(),

    //These will be queried from listening apps when triggers fire.
    var useProgressBar: Boolean = false,
    var actionKeys: String = "",        // delimiter = ','
    var progressContentUri: String = "",
    var actionContentUri: String = ""
)
