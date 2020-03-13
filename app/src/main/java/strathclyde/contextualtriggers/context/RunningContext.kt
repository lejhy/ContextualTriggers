package strathclyde.contextualtriggers.context

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence

class RunningContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.RUNNING),
    "RUNNING CONTEXT FENCE",
    "RUNNING CONTEXT ACTION"
)