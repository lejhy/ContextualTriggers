package strathclyde.contextualtriggers.context.steps

import android.app.IntentService
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class StepsDetector(
) :  SensorEventListener, IntentService("Detected Steps") {
    override fun onHandleIntent(p0: Intent?) {
  startStepCount()
    }

    var sensorManager: SensorManager? = null

    override fun onSensorChanged(p0: SensorEvent?) {
        val intent = Intent(BasicStepsContext.STEPS.STEPS_DETECTED_INTENT)
        application.sendBroadcast(intent)


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}



     private fun startStepCount(){
        Log.d("Registered ", "STEPS_COUNTER manager")
         sensorManager = application.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager?

         val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (stepsSensor != null) run {
            sensorManager?.registerListener(
                this,
                stepsSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

}
