package strathclyde.contextualtriggers.context.battery

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import strathclyde.contextualtriggers.context.Context

class BatteryLevelContext(
    private val application: Application
) : Context() {

    private var batterLevel = 0

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            intent?.let { i ->
                val level: Int = i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale: Int = i.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val newBatterLevel = level * 100 / scale
                if (batterLevel != newBatterLevel) {
                    update(newBatterLevel)
                    batterLevel = newBatterLevel
                    Log.d("BATTERY CONTEXT", "Updated")
                }
            }
        }
    }

    override fun onStart() {
        Log.d("Registered ", "ACTION_BATTERY_CHANGED receiver")
        application.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
    }
}