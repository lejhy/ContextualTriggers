package strathclyde.contextualtriggers.context

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence

class OnFootContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.ON_FOOT),
    "ON FOOT CONTEXT FENCE",
    "ON FOOT CONTEXT ACTION"
)
