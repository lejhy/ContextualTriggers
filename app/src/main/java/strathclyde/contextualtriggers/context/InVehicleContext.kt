package strathclyde.contextualtriggers.context

import android.app.Application
import com.google.android.gms.awareness.fence.DetectedActivityFence

class InVehicleContext(
    application: Application
): FenceContext(
    application,
    DetectedActivityFence.during(DetectedActivityFence.IN_VEHICLE),
    "IN VEHICLE CONTEXT FENCE",
    "IN VEHICLE CONTEXT ACTION"
)
