package strathclyde.contextualtriggers.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore

@Entity
data class Trigger(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var title: String = "",
    var content: String = "",
    var iconKey: String = "",
    var active: Boolean = false
)
