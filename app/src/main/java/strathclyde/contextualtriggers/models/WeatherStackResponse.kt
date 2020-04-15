package strathclyde.contextualtriggers.models

import com.google.gson.annotations.Expose

data class WeatherStackResponse(
    @Expose
    val current: Current?
) : IWeather {

    override fun temperature(): Float? {
        return current?.temperature
    }

    override fun windSpeed(): Float? {
        return current?.wind_speed?.times((1000.0f / 3600.0f)) // km/h to m/s
    }

    override fun cloudsPercentage(): Float? {
        return current?.cloudcover
    }

    override fun rainMM(): Float? {
        return current?.precip
    }

    override fun description(): String? {
        return null
    }
}

class Current {

    var observation_time: String? = null
    var temperature = 0f
    var weather_code = 0f
    var weather_icons = ArrayList<Any>()
    var weather_descriptions = ArrayList<Any>()
    var wind_speed = 0f
    var wind_degree = 0f
    var wind_dir: String? = null
    var pressure = 0f
    var precip = 0f
    var humidity = 0f
    var cloudcover = 0f
    var feelslike = 0f
    var uv_index = 0f
    var visibility = 0f
    var is_day: String? = null

}