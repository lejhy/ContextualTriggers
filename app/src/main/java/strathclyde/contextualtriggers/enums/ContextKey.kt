package strathclyde.contextualtriggers.enums

import strathclyde.contextualtriggers.context.*
import strathclyde.contextualtriggers.context.activity.*
import strathclyde.contextualtriggers.context.battery.BatteryLevelContext
import strathclyde.contextualtriggers.context.headphones.HeadphonesContext
import strathclyde.contextualtriggers.context.steps.BasicStepsContext
import strathclyde.contextualtriggers.context.time.TimeContext
import strathclyde.contextualtriggers.context.weather.HazeContext
import strathclyde.contextualtriggers.context.weather.RainContext
import strathclyde.contextualtriggers.context.weather.SunnyContext
import strathclyde.contextualtriggers.context.weather.TemperatureContext

enum class ContextKey {
    IN_VEHICLE, ON_BICYCLE, ON_FOOT, RUNNING, STILL, WALKING, SUNNY, HAZE, RAIN, TEMPERATURE, BATTERY_LEVEL, HEADPHONES, TIME, STEPS;

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
            BATTERY_LEVEL -> BatteryLevelContext::class.java
            HEADPHONES -> HeadphonesContext::class.java
            STEPS -> BasicStepsContext::class.java
            TIME -> TimeContext::class.java
        }
    }
}
