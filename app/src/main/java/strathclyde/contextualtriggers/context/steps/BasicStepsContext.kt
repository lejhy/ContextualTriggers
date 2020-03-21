package strathclyde.contextualtriggers.context.steps

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import strathclyde.contextualtriggers.context.Context
import kotlin.math.roundToInt


class BasicStepsContext(
    private val application: Application
) : Context() {
    object STEPS {
        const val STEPS_DETECTED_INTENT: String = "strathclyde.contextualtriggers.context.STEPS_DETECTED"
    }


    val goal: Int = 10
    private var currentSteps: Double = 0.0
    private var lastSteps: Double = 0.0

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            if (intent?.action == STEPS.STEPS_DETECTED_INTENT) {
                currentSteps++
                val newStepsPercent = ((currentSteps / goal) * 100).roundToInt()
                if (currentSteps != lastSteps && currentSteps != 0.0) {
                    update(newStepsPercent)
                    lastSteps = currentSteps
                    Log.d("STEPS CONTEXT", "Updated")
                    Log.d("Current Steps", "" + currentSteps)

                }
            }
        }
    }

    override fun onStart() {
        Log.d("Registered ", "STEPS_DETECTED receiver")
        val detector = Intent(application, StepsDetector::class.java)
        application.startService(detector)
        val filter = IntentFilter()
        filter.addAction(STEPS.STEPS_DETECTED_INTENT)
        application.registerReceiver(receiver, filter)
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)

    }
}
