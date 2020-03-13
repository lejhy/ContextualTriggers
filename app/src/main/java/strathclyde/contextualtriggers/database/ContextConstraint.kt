package strathclyde.contextualtriggers.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContextConstraint(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var contextKey: String = "",
    var greaterThanOrEqualTo: Int = 0,
    var lessThanOrEqualTo: Int = 0,
    var triggerId: Long = 0L
)
