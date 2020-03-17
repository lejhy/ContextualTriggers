package strathclyde.contextualtriggers.enums

import strathclyde.contextualtriggers.context.*
import strathclyde.contextualtriggers.context.weatherContext.HazeContext
import strathclyde.contextualtriggers.context.weatherContext.RainContext
import strathclyde.contextualtriggers.context.weatherContext.SunnyContext
import strathclyde.contextualtriggers.context.weatherContext.TemperatureContext

enum class ContextKey {
    IN_VEHICLE, ON_BICYCLE, ON_FOOT, RUNNING, STILL, WALKING, SUNNY, HAZE, RAIN, TEMPERATURE;

    fun resolveClass(): Class<out Context> {
        return when (this) {
            IN_VEHICLE -> InVehicleContext::class.java
            ON_BICYCLE -> OnBicycleContext::class.java
            ON_FOOT -> OnFootContext::class.java
            RUNNING -> RunningContext::class.java
            STILL -> StillContext::class.java
            WALKING -> WalkingContext::class.java
            SUNNY -> SunnyContext::class.java
            HAZE -> HazeContext::class.java
            RAIN -> RainContext::class.java
            TEMPERATURE -> TemperatureContext::class.java
        }
    }
}
