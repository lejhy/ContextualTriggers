package strathclyde.contextualtriggers.enums

import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.context.activity.*
import strathclyde.contextualtriggers.context.battery.BatteryLevelContext
import strathclyde.contextualtriggers.context.headphones.HeadphonesContext
import strathclyde.contextualtriggers.context.location.AtHouseContext
import strathclyde.contextualtriggers.context.steps.BasicStepsContext
import strathclyde.contextualtriggers.context.steps.DayGoalContext
import strathclyde.contextualtriggers.context.steps.NextGoalCompletionContext
import strathclyde.contextualtriggers.context.steps.WeekGoalContext
import strathclyde.contextualtriggers.context.time.TimeContext
import strathclyde.contextualtriggers.context.weather.*

enum class ContextKey {
    IN_VEHICLE, ON_BICYCLE, ON_FOOT, RUNNING, STILL, WALKING, SUNNY, HAZE, RAIN, TEMPERATURE, WIND_SPEED, CLOUDS, BATTERY_LEVEL, HEADPHONES, TIME, STEPS, AT_HOME, DAY_COMPLETION, WEEK_COMPLETION, NEXT_GOAL_COMPLETION;

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
            WIND_SPEED -> WindContext::class.java
            CLOUDS -> CloudsContext::class.java
            BATTERY_LEVEL -> BatteryLevelContext::class.java
            HEADPHONES -> HeadphonesContext::class.java
            STEPS -> BasicStepsContext::class.java
            TIME -> TimeContext::class.java
            AT_HOME -> AtHouseContext::class.java
            DAY_COMPLETION -> DayGoalContext::class.java
            WEEK_COMPLETION -> WeekGoalContext::class.java
            NEXT_GOAL_COMPLETION -> NextGoalCompletionContext::class.java
        }
    }
}
