package strathclyde.contextualtriggers.context.weather

import android.app.Application

class RainContext(
    application: Application
) : WeatherDescriptionContext(
    application,
    "RAIN"
)