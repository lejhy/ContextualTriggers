package strathclyde.contextualtriggers.trigger

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import strathclyde.contextualtriggers.MainActivity
import strathclyde.contextualtriggers.R
import strathclyde.contextualtriggers.context.Context

class WalkingTrigger(
    private val application: Application
): Trigger() {
    override fun update(context: Context, value: Int) {
        val pendingIntent: PendingIntent =
            Intent(application, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(application, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(application, application.getString(R.string.channel_id))
            .setContentTitle("Walking")
            .setContentText(String.format("Value %d", value))
            .setSmallIcon(R.drawable.ic_notification_important_black_24dp)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager: NotificationManager = application.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(R.integer.walkingNotificationId, notification)
    }
}
