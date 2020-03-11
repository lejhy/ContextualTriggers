package strathclyde.contextualtriggers

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import strathclyde.contextualtriggers.context.WalkingContext
import strathclyde.contextualtriggers.trigger.WalkingTrigger

class MainService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.i("MainService", "onCreate called")
        val pendingIntent: PendingIntent =
                Intent(this, MainActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(this, 0, notificationIntent, 0)
                }

        val notification: Notification = Notification.Builder(this, getString(R.string.channel_id))
                .setContentTitle(getString(R.string.contextualTriggers))
                .setContentText(getString(R.string.ContextualTriggersAreNowRunning))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification_important_black_24dp)
                .build()

        startForeground(R.integer.contextualTriggersNotificationId, notification)

        //THE REST OF FUNCTION IS JUST FOR TESTING
        val context = WalkingContext(application)
        val trigger = WalkingTrigger(application)
        context.register(trigger)
    }

    override fun onDestroy() {
        super.onCreate()
        Log.i("MainService", "onDestroy called")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }
}
