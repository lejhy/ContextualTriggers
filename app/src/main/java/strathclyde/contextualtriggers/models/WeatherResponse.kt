package strathclyde.contextualtriggers.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    @Expose
    val weather: ArrayList<Weather>? = null,

    @Expose
    val main: Main?,

    @Expose
    val wind: Wind?,

    @Expose
    val clouds: Clouds?,

    @Expose
    val rain: Rain?

)

class Weather {
    var main: String? = null
}

class Clouds {
    @SerializedName("all")
    var percentage = 0f

}

class Rain {
    @SerializedName("3h")
    var h3 = 0f

}

class Wind {
    var speed = 0f
}

class Main {
    var temp = 0f
}