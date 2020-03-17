package strathclyde.contextualtriggers.context;

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter

abstract class LocationBasedContext (
    private val application: Application,
    private val actionKey: String
): Context() {

    abstract fun useLocation(lat: Long, long: Long) : Int

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            update(useLocation(0,0));
        }
    }
    override fun onStart(){
        application.registerReceiver(receiver, IntentFilter(actionKey))

    }

    override fun onStop(){
        application.unregisterReceiver(receiver)

    }
}
