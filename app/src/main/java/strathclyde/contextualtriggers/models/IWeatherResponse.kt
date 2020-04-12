package strathclyde.contextualtriggers.models

interface IWeatherResponse {

    val weather: ArrayList<Weather>?

    val main: Main?

    val wind: Wind?

    val clouds: Clouds?

    val rain: Rain?
}