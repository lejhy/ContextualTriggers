package strathclyde.contextualtriggers.models

interface IWeather {
    fun description(): String?
    fun temperature(): Float?
    fun windSpeed(): Float?
    fun cloudsPercentage(): Float?
    fun rainMM(): Float?
}