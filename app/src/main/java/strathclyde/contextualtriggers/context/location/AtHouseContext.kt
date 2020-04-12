package strathclyde.contextualtriggers.context.location

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import strathclyde.contextualtriggers.database.LocationEntry
import strathclyde.contextualtriggers.database.LocationEntryDao
import strathclyde.contextualtriggers.database.MainDatabase
import java.util.*
import kotlin.collections.HashMap

class AtHouseContext(
    application: Application
) : LocationBasedContext(
    application
) {
    private var history: MutableList<LocationEntry> = LinkedList()
    private lateinit var locationEntryDao: LocationEntryDao
    private val tolerance: Double = 0.5
    override fun useLocation(lat: Double, long: Double): Int {
        //create region known as home location
        val home: Pair<Double, Double> = getHomeLocation(lat, long)
        //check if lat lon in in tolerance for home region
        return if (
            lat + tolerance > home.first &&
            lat - tolerance < home.first &&
            long + tolerance > home.second &&
            long - tolerance < home.second
        ) 1 else 0
    }

    private fun getHomeLocation(lat: Double, long: Double): Pair<Double, Double> {
        val currentLocation = LocationEntry()
        currentLocation.lat = lat
        currentLocation.lon = long
        history.add(currentLocation)
        DBSave().execute(this)
        var home: Pair<Double, Double> = Pair(0.0, 0.0)
        val mapping: MutableMap<Pair<Double, Double>, Int> = HashMap()
        for (current in history) {
            val locationInstance = Pair(current.lat, current.lon)
            if (mapping.contains(locationInstance))
                mapping.replace(locationInstance, mapping.getValue(locationInstance) + 1)
        }
        var max = 0
        for (current in mapping.entries)
            if (current.value > max) {
                max = current.value
                home = current.key
            }
        return home
    }


    override fun onStart() {
        super.onStart()
        locationEntryDao = MainDatabase.getInstance(super.application).locationEntryDao
        DBLoad().execute(this)
    }

    override fun onStop() {
        super.onStop()
        DBSave().execute(this)
    }

    class DBSave : AsyncTask<AtHouseContext, Void, Void?>() {
        override fun doInBackground(vararg p0: AtHouseContext?): Void? {
            val master = p0.first()
            master?.history?.forEach { master.locationEntryDao.update(it) }
            Log.d("AT HOME CONTEXT", "save history ${master?.history}")
            return null
        }

    }

    class DBLoad : AsyncTask<AtHouseContext, Void, Void?>() {
        override fun doInBackground(vararg p0: AtHouseContext?): Void? {
            val master = p0.first()

            if (master != null)
                master.history = master.locationEntryDao.getAll()
            Log.d("AT HOME CONTEXT", "fetch history ${master?.history}")
            return null
        }

    }
//𝓤𝔀𝓤
}