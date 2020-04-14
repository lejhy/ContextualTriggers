package strathclyde.contextualtriggers.context.steps

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import strathclyde.contextualtriggers.broadcasters.HistoryJobService
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.utils.JobUtils

class DayGoalContext(private val application: Application) : Context() {

    companion object {
        var lastStep: Double = 0.0
        var lastTarget: Double = 0.0
        var lastGoal: String = ""
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent) {
            lastStep = intent.extras?.get("day_steps").toString().toDouble()
            lastTarget = intent.extras?.get("day_target").toString().toDouble()
            lastGoal = intent.extras?.get("day_goal").toString()

            val completionRatio = ((lastStep / lastTarget) * 100).toInt()
            Log.d("DAY CONTEXT", "Updated completion ratio $completionRatio")
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