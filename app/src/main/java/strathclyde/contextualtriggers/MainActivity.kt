package strathclyde.contextualtriggers

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(application)

        application.startService(Intent(application, MainService::class.java))
        Toast.makeText(this, "Contextual triggers are running", Toast.LENGTH_LONG).show();
        finish();
    }

    private fun createNotificationChannel(application: Application) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            application.getString(R.string.channel_id),
            application.getString(R.string.channel_name),
            importance
        )
        val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
