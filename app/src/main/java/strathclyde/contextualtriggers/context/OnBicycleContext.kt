package strathclyde.contextualtriggers.context

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence

class OnBicycleContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.ON_BICYCLE),
    "ON BICYCLE CONTEXT FENCE",
    "ON BICYCLE CONTEXT ACTION"
)
