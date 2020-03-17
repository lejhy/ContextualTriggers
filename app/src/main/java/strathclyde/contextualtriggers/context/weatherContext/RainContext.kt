package strathclyde.contextualtriggers.context.weatherContext

import android.app.Application

class RainContext(
    application: Application
) : WeatherDescriptionContext(
    application,
    "RAIN"
)