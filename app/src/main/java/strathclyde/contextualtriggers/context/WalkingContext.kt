package strathclyde.contextualtriggers.context

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence

class WalkingContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.WALKING),
    "WALKING CONTEXT FENCE",
    "WALKING CONTEXT ACTION"
)