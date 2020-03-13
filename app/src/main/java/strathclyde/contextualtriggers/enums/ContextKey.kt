package strathclyde.contextualtriggers.enums

import strathclyde.contextualtriggers.context.*

enum class ContextKey {
    IN_VEHICLE, ON_BICYCLE, ON_FOOT, RUNNING, STILL, WALKING;

    fun resolveClass(): Class<out Context> {
        return when (this) {
            IN_VEHICLE -> InVehicleContext::class.java
            ON_BICYCLE -> OnBicycleContext::class.java
            ON_FOOT -> OnFootContext::class.java
            RUNNING -> RunningContext::class.java
            STILL -> StillContext::class.java
            WALKING -> WalkingContext::class.java

        }
    }
}
