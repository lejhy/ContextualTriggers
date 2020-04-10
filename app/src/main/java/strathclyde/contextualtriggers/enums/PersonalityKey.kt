package strathclyde.contextualtriggers.enums

enum class PersonalityKey {
    MOTIVATED, LAZY, DEFAULT;

    fun resolve(): String {
        return when (this) {
            MOTIVATED -> "motivated"
            LAZY -> "lazy"
            DEFAULT -> "default"
        }
    }
}