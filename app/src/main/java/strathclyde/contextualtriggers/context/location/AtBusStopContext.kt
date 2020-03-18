package strathclyde.contextualtriggers.context.location

import android.app.Application
import strathclyde.contextualtriggers.context.location.LocationBasedContext

class AtBusStopContext(
    application: Application
): LocationBasedContext(
    application
){
    override fun useLocation(lat: Double, long: Double): Int {
        TODO("Not yet implemented")
    }

}