package strathclyde.contextualtriggers.context.location

import android.app.Application
import java.util.*
import kotlin.collections.HashMap

class AtHouseContext(
    application: Application
) : LocationBasedContext(
    application
) {
    private var history: MutableList<Pair<Double, Double>> = LinkedList()
    private val tolerance: Double = 0.5
    override fun useLocation(lat: Double, long: Double): Int {
        //create region known as home location
        val home: Pair<Double, Double> = getHomeLocation(lat, long)
        //check if lat lon in in tollerance for home region
        return if (
            lat + tolerance > home.first &&
            lat - tolerance < home.first &&
            long + tolerance > home.second &&
            long - tolerance < home.second
        ) 1 else 0
    }

    private fun getHomeLocation(lat: Double, long: Double): Pair<Double, Double> {
        history.add(Pair(lat, long))
        var home: Pair<Double, Double> = Pair(0.0, 0.0)
        val mapping: MutableMap<Pair<Double, Double>, Int> = HashMap()
        for (current in history)
            if (mapping.contains(current))
                mapping.replace(current, mapping.getValue(current) + 1)
        var max = 0
        for (current in mapping.entries)
            if (current.value > max) {
                max = current.value
                home = current.key
            }
        return home
    }


//𝓤𝔀𝓤
}