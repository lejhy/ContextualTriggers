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

) : IWeather {
    override fun description(): String? {
        return weather?.get(0)?.main
    }

    override fun temperature(): Float? {
        return main?.temp
    }

    override fun windSpeed(): Float? {
        return wind?.speed
    }

    override fun cloudsPercentage(): Float? {
        return clouds?.percentage
    }

    override fun rainMM(): Float? {
        return rain?.h3
    }
}

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