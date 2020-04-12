package strathclyde.contextualtriggers.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WeatherResponse(
    @Expose
    override val weather: ArrayList<Weather>? = null,

    @Expose
    override val main: Main?,

    @Expose
    override val wind: Wind?,

    @Expose
    override val clouds: Clouds?,

    @Expose
    override val rain: Rain?

): IWeatherResponse

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