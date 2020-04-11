package strathclyde.contextualtriggers.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import strathclyde.contextualtriggers.models.WeatherResponse

interface OpenWeatherApiService {
    @GET("data/2.5/weather?")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appid: String
    ): Response<WeatherResponse>
}