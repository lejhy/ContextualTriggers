package strathclyde.contextualtriggers.context

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence

class StillContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.STILL),
    "STILL CONTEXT FENCE",
    "STILL CONTEXT ACTION"
)
