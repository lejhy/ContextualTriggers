package strathclyde.contextualtriggers.context

import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.awareness.Awareness
import com.google.android.gms.awareness.fence.AwarenessFence
import com.google.android.gms.awareness.fence.DetectedActivityFence
import com.google.android.gms.awareness.fence.FenceState
import com.google.android.gms.awareness.fence.FenceUpdateRequest


class WalkingContext(
    private val application: Application
): Context() {

    private val fence: AwarenessFence = DetectedActivityFence.during(DetectedActivityFence.WALKING)
    private val fenceKey = "WALKING CONTEXT FENCE KEY"
    private val actionKey = "WALKING CONTEXT ACTION KEY"

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: Intent?) {
            val fenceState = FenceState.extract(intent)
            if (TextUtils.equals(fenceState.fenceKey, fenceKey)) {
                val fenceValue = when (fenceState.currentState) {
                    FenceState.FALSE -> 0
                    FenceState.TRUE -> 1
                    else -> -1
                }
                update(fenceValue)
            }
        }
    }
    private val pendingIntent = PendingIntent.getBroadcast(
        application,
        0,
        Intent(actionKey),
        0
    )

    override fun onStart() {
        application.registerReceiver(receiver, IntentFilter(actionKey))
        Awareness.getFenceClient(application).updateFences(FenceUpdateRequest.Builder()
            .addFence(fenceKey, fence, pendingIntent)
            .build()
        )
            .addOnSuccessListener { Log.i("WalkingContext", "success") }
            .addOnFailureListener { Log.i("WalkingContext", "failure: $it") }
    }

    override fun onStop() {
        application.unregisterReceiver(receiver)
        Awareness.getFenceClient(application).updateFences(FenceUpdateRequest.Builder()
            .removeFence(fenceKey)
            .build()
        )
            .addOnSuccessListener { Log.i("WalkingContext", "success") }
            .addOnFailureListener { Log.i("WalkingContext", "failure: $it") }
    }
}
