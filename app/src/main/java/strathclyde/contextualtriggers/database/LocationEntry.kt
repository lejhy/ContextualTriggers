package strathclyde.contextualtriggers.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var lat: Double = 0.0,
    var lon: Double = 0.0
)