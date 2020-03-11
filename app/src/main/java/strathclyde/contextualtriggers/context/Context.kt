package strathclyde.contextualtriggers.context

import strathclyde.contextualtriggers.trigger.Trigger

abstract class Context {
    private val triggers: MutableSet<Trigger> = mutableSetOf()

    abstract fun onStart()
    abstract fun onStop()

    protected fun update(value: Int) {
        triggers.forEach {
            it.update(this, value)
        }
    }

    fun register(trigger: Trigger): Boolean {
        val result = triggers.add(trigger)
        if (result && triggers.size == 1) {
            onStart()
        }
        return result
    }

    fun unregister(trigger: Trigger): Boolean {
        val result = triggers.remove(trigger)
        if (result && triggers.size == 0) {
            onStop()
        }
        return result
    }
}
