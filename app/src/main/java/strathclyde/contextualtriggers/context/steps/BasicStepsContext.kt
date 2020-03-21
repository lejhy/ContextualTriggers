package strathclyde.contextualtriggers.context.steps

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import strathclyde.contextualtriggers.context.Context
import kotlin.math.roundToInt


class BasicStepsContext(
    private val application: Application
) : Context(), SensorEventListener {

    var sensorManager: SensorManager? = null
    val goal: Int = 100
    private var currentSteps: Double = 0.0
    private var lastSteps: Double = 0.0
    private val stepsIntent: String = "STEPS_DETECTED"

    override fun onSensorChanged(p0: SensorEvent?) {
        currentSteps++
        Log.d("STEPS NUMBER", "" + currentSteps)
        val intent = Intent(this.stepsIntent)
        application.sendBroadcast(intent)


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            val newStepsPepcent = ((currentSteps / goal)*100).roundToInt()
            Log.d("STEPS NUMBER TEST", "" + currentSteps)
            if (currentSteps != lastSteps && currentSteps!= 0.0) {
                update(newStepsPepcent)
                lastSteps = currentSteps
                Log.d("STEPS CONTEXT", "Updated")
            }
        }
    }

    override fun onStart() {
        Log.d("Registered ", "STEPS_DETECTED receiver")
        application.registerReceiver(receiver, IntentFilter(this.stepsIntent))

        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (stepsSensor != null) run {
            sensorManager?.registerListener(
                this,
                stepsSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onStop() {
        sensorManager?.unregisterListener(this)
        application.unregisterReceiver(receiver)

    }
}
