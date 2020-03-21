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
Checks for minute, hour and day changes. Broadcasts all 3 together in format DSM

d: if day changed, d in 1..7 where d is ordinal for DayOfWeek + 1 (i.e. monday=1,tuesday=2), else 0
h: if hour changes, 1 else 0
m: if minute changed, 1 else 0 **/
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

        var updateValue: Int

        updateValue = if (currentTime.dayOfWeek == oldTime.dayOfWeek) 0 else currentTime.dayOfWeek.ordinal+1
        updateValue = updateValue * 10 + if (currentTime.hour == oldTime.hour) 0 else 1
        updateValue = updateValue * 10 + if (currentTime.minute == oldTime.minute) 0 else 1


        if (updateValue > 0) {
            Log.d(
                "TimeBroadcast",
                "Change of time. updateValue: $updateValue,  oldTime: $oldTime, newTime: $newTime"
            )
            update(updateValue)
        }
    }


}