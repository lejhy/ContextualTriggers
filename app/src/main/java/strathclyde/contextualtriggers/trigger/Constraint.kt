package strathclyde.contextualtriggers.trigger

class Constraint(
    private val lessThanOrEqualTo: Int,
    private val greaterThanOrEqualTo: Int
) {
    private var _state = false
    val state: Boolean
        get() = _state

    fun evaluate(value: Int): Boolean {
        _state = (value in greaterThanOrEqualTo..lessThanOrEqualTo)
        return _state
    }
}
