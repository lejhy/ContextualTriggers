package strathclyde.contextualtriggers.trigger

import strathclyde.contextualtriggers.context.Context

abstract class Trigger {
    abstract fun update(context: Context, value: Int)
}
