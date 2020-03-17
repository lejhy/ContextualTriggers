import android.app.Application
import strathclyde.contextualtriggers.context.LocationBasedContext

class inHouseContext(
    application: Application
): LocationBasedContext(
    application,
    "IN HOUSE CONTEXT ACTION"
){
    override fun useLocation(lat: Long, long: Long): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}