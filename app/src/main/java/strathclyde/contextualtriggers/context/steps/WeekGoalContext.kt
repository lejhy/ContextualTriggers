package strathclyde.contextualtriggers.context.steps

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import strathclyde.contextualtriggers.broadcasters.HistoryJobService
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.utils.JobUtils

class WeekGoalContext(private val application: Application) : Context() {

    companion object {
        var lastDays: Double = 0.0
        var lastSteps: Double = 0.0
        var lastGoals: Double = 0.0
        var lastCompleted: Double = 0.0
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent) {
            lastDays = intent.extras?.get("days").toString().toDouble()
            lastSteps = intent.extras?.get("steps").toString().toDouble()
            lastGoals = intent.extras?.get("target").toString().toDouble()
            lastCompleted = intent.extras?.get("completed").toString().toDouble()

            val completionRatio = ((lastDays / lastCompleted) * 100).toInt()
            Log.d("WEEK CONTEXT", "Updated completion ratio $completionRatio")
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