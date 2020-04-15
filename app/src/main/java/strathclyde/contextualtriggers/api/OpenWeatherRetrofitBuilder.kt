package strathclyde.contextualtriggers.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenWeatherRetrofitBuilder {
    const val BASE_URL = "https://api.openweathermap.org/"

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val OPEN_WEATHER_API_SERVICE: OpenWeatherApiService by lazy {
        retrofitBuilder.build().create(OpenWeatherApiService::class.java)
    }
}