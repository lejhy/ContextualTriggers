package strathclyde.contextualtriggers.context.activity

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence
import strathclyde.contextualtriggers.context.FenceContext

class StillContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.STILL),
    "STILL CONTEXT FENCE",
    "STILL CONTEXT ACTION"
)
