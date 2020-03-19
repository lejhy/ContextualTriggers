package strathclyde.contextualtriggers.context.headphones

import android.app.Application
import com.google.android.gms.awareness.fence.HeadphoneFence
import com.google.android.gms.awareness.state.HeadphoneState
import strathclyde.contextualtriggers.context.FenceContext

class HeadphonesContext (
    application: Application
): FenceContext(
    application,
    HeadphoneFence.during(HeadphoneState.PLUGGED_IN),
    "HEADPHONES CONTEXT FENCE",
    "HEADPHONES CONTEXT ACTION"
)
