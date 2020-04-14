package strathclyde.contextualtriggers.context.time

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import strathclyde.contextualtriggers.context.Context
import java.time.LocalDateTime


/**
 *
Checks for minute, hour and day changes. Broadcasts all 3 together in format DHHMM

 **/
class TimeContext(
    private val application: Application
) : Context() {
    private var currentTime = LocalDateTime.now()
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context?, intent: Intent?) {
            onTimeChanged(LocalDateTime.now())
        }
    }

    override fun onStart() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_CHANGED)
        filter.addAction(Intent.ACTION_DATE_CHANGED)
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
        filter.addAction(Intent.ACTION_TIME_TICK)
        application.registerReceiver(receiver, filter)
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
    }


    fun onTimeChanged(newTime: LocalDateTime) {
        val oldTime = currentTime
        currentTime = newTime

        var updateValue: Int = currentTime.dayOfWeek.ordinal + 1
        updateValue = updateValue * 100 + currentTime.hour
        updateValue = updateValue * 100 + currentTime.minute

        Log.d(
            "TimeBroadcast",
            "Change of time. updateValue: $updateValue,  oldTime: $oldTime, newTime: $newTime"
        )
        update(updateValue)

    }


}