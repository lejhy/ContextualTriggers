package strathclyde.contextualtriggers.trigger

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.util.Log
import strathclyde.contextualtriggers.R
import strathclyde.contextualtriggers.UserPersonality.UserPersonalityDecider
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.database.TriggerWithContextConstraints
import strathclyde.contextualtriggers.enums.ContextKey
import strathclyde.contextualtriggers.enums.IconKey

class Trigger(
    private val application: Application,
    contexts: List<Context>,
    triggerWithContextConstraints: TriggerWithContextConstraints
) {
    private val title = triggerWithContextConstraints.trigger.title
    private val content = triggerWithContextConstraints.trigger.content
    private val altContent = triggerWithContextConstraints.trigger.altContent
    private val iconKey = IconKey.valueOf(triggerWithContextConstraints.trigger.iconKey)
    private val active = triggerWithContextConstraints.trigger.active
    private val contextConstraintsMap: MutableMap<Context, MutableList<Constraint>> = mutableMapOf()

    private val notificationManager =
        application.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        val contextMap = contexts.associateBy { context -> context::class.java }
        triggerWithContextConstraints.contextConstraints.forEach { constraint ->
            val context =
                contextMap[ContextKey.valueOf(constraint.contextKey).resolveClass()] ?: error(
                    "Context doesn't exist: " + constraint.contextKey
                )
            var constraintList = contextConstraintsMap[context]
            if (constraintList == null) {
                constraintList = mutableListOf()
                contextConstraintsMap[context] = constraintList
                if (active) context.register(this) //TODO dynamic activation and deactivation
            }
            constraintList.add(
                Constraint(
                    constraint.lessThanOrEqualTo,
                    constraint.greaterThanOrEqualTo
                )
            )
        }
    }

    fun update(context: Context, value: Int) {
        contextConstraintsMap[context]?.forEach { constraint ->
//            Log.i("Trigger "+ title, "forEach first")
            constraint.evaluate(value)
        }
//        Log.i("Trigger "+ title, "update")
        contextConstraintsMap.forEach { (_, constraints) ->
//            Log.i("Trigger "+ title, "forEach second")
            if (!constraints.map { constraint -> constraint.state }.reduce { acc, state -> acc || state }) return
        }
        Log.i("Trigger " + title, "notification")
        createNotification()
    }

    private fun createNotification() {
        UserPersonalityDecider
        val notification: Notification =
            if (altContent == "" || UserPersonalityDecider.getDecider(application.applicationContext)
                    .isPositivePersonality()
            ) {
                Notification.Builder(application, application.getString(R.string.channel_id))
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(iconKey.resolveResource())
                    .build()
            } else {
                Notification.Builder(application, application.getString(R.string.channel_id))
                    .setContentTitle(title)
                    .setContentText(altContent)
                    .setSmallIcon(iconKey.resolveResource())
                    .build()
            }

        notificationManager.notify(R.integer.walkingNotificationId, notification)
    }
}
