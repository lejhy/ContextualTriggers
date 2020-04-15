package strathclyde.contextualtriggers.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherStackRetrofitBuilder {
    const val BASE_URL = "http://api.weatherstack.com/"

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val WEATHER_STACK_API_SERVICE: WeatherStackApiService by lazy {
        retrofitBuilder.build().create(WeatherStackApiService::class.java)
    }
}