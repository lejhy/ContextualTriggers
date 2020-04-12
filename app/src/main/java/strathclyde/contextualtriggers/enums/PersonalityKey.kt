package strathclyde.contextualtriggers.enums

enum class PersonalityKey {
    MOTIVATED, LAZY, DEFAULT;

    fun resolve(): Int {
        return when (this) {
            MOTIVATED -> 0
            LAZY -> 1
            DEFAULT -> -1
        }
    }
}