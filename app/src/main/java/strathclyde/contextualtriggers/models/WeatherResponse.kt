package strathclyde.contextualtriggers.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    @Expose
    val weather: ArrayList<Weather>? = null,

    @Expose
    val main: Main? = null,

    @Expose
    val wind: Wind? = null,

    @Expose
    val clouds: Clouds?,

    @Expose
    val rain: Rain?
)

class Weather {
    @SerializedName("id")
    var id = 0

    @SerializedName("main")
    var main: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("icon")
    var icon: String? = null
}

class Clouds {
    @SerializedName("all")
    var all = 0f
}

class Rain {
    @SerializedName("3h")
    var h3 = 0f
}

class Wind {
    @SerializedName("speed")
    var speed = 0f

    @SerializedName("deg")
    var deg = 0f
}

class Main {
    @SerializedName("temp")
    var temp = 0f

    @SerializedName("humidity")
    var humidity = 0f

    @SerializedName("pressure")
    var pressure = 0f

    @SerializedName("temp_min")
    var temp_min = 0f

    @SerializedName("temp_max")
    var temp_max = 0f
}