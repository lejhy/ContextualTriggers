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
    var iconKey: String = "",
    var active: Boolean = false,
    var altContent: String = "",
    var success: Int = PersonalityKey.MOTIVATED.resolve(),
    var failure: Int = PersonalityKey.LAZY.resolve(),

    //These will be queried from listening apps when triggers fire.
    var useProgressBar: Boolean = false,      /*notification progress-bar.   Will request currentValue: Int,
                                                                                           maximumValue: Int*/
    var useBadging: Boolean = false,           /*notification badging.        Will request badgingValue: Int
                                                                                                            */
    var actionkeys: List<String> = listOf()    /*notification action buttons. Will request buttonText: String,
                                                                                         buttonCallback (keyName: String -> Unit)*/
)
