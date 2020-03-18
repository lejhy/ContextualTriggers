package strathclyde.contextualtriggers.context.weather

import android.app.Application

class SunnyContext(
    application: Application
) : WeatherDescriptionContext(
    application,
    "SUNNY"
)