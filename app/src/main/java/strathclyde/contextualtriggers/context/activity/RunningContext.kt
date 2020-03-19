package strathclyde.contextualtriggers.context.activity

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence
import strathclyde.contextualtriggers.context.FenceContext

class RunningContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.RUNNING),
    "RUNNING CONTEXT FENCE",
    "RUNNING CONTEXT ACTION"
)
