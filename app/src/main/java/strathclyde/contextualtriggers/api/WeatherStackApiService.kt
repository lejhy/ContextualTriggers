package strathclyde.contextualtriggers.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import strathclyde.contextualtriggers.models.WeatherStackResponse

interface WeatherStackApiService {
    @GET("current?")
    suspend fun getWeather(
        @Query("access_key") access_key: String,
        @Query("query") query: String
    ): Response<WeatherStackResponse>
}