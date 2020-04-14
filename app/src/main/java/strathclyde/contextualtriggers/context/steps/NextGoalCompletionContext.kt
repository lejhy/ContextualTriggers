package strathclyde.contextualtriggers.context.steps

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import strathclyde.contextualtriggers.broadcasters.HistoryJobService
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.utils.JobUtils

class NextGoalCompletionContext(private val application: Application) : Context() {

    companion object {
        var lastLargestTarget: Double = 0.0
        var lastLargestGoal: String = ""
        var lastSteps: Double = 0.0
        var lastCompleted: Double = 0.0
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent) {
            lastLargestGoal = intent.extras?.get("next_largest_goal_name").toString()
            lastLargestTarget = intent.extras?.get("next_largest_goal_steps").toString().toDouble()
            lastSteps = intent.extras?.get("day_steps").toString().toDouble()
            lastCompleted = intent.extras?.get("completed").toString().toDouble()

            val completionRatio = ((lastSteps / lastLargestTarget) * 100).toInt()
            Log.d("NEXT GOAL COMPLETION CONTEXT", "Updated completion ratio $completionRatio")
            update(completionRatio)
        }
    }

    override fun onStart() {
        application.registerReceiver(receiver, IntentFilter(HistoryJobService.HISTORY_BROADCAST_ID))

        JobUtils.startHistoryJob(application)
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
    }
}