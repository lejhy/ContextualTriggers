package strathclyde.contextualtriggers.context.weatherContext

import android.app.Application

class SunnyContext(
    application: Application
) : WeatherDescriptionContext(
    application,
    "SUNNY"
)