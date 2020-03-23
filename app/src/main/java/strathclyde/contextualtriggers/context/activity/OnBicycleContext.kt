package strathclyde.contextualtriggers.context.activity

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence
import strathclyde.contextualtriggers.context.FenceContext

class OnBicycleContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.ON_BICYCLE),
    "ON BICYCLE CONTEXT FENCE",
    "ON BICYCLE CONTEXT ACTION"
)
