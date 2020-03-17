import android.app.Application
import strathclyde.contextualtriggers.context.LocationBasedContext

class atBusStopContext(
    application: Application
): LocationBasedContext(
    application,
    "AT BUS STOP CONTEXT ACTION"
){
    override fun useLocation(lat: Long, long: Long): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}