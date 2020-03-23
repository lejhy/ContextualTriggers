package strathclyde.contextualtriggers.context.activity

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence
import strathclyde.contextualtriggers.context.FenceContext

class InVehicleContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.IN_VEHICLE),
    "IN VEHICLE CONTEXT FENCE",
    "IN VEHICLE CONTEXT ACTION"
)
