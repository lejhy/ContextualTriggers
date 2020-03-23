package strathclyde.contextualtriggers.context.activity

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence
import strathclyde.contextualtriggers.context.FenceContext

class OnFootContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.ON_FOOT),
    "ON FOOT CONTEXT FENCE",
    "ON FOOT CONTEXT ACTION"
)
