package strathclyde.contextualtriggers.enums

import strathclyde.contextualtriggers.R

enum class IconKey {
    NOTIFICATION_IMPORTANT;

    fun resolveResource(): Int {
        return when (this) {
            NOTIFICATION_IMPORTANT -> R.drawable.ic_notification_important_black_24dp
        }
    }
}
