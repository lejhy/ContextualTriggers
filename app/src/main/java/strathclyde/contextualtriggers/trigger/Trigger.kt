package strathclyde.contextualtriggers.trigger

import android.app.Application
import android.app.Notification
import android.app.Notification.Action.Builder
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.util.Log
import strathclyde.contextualtriggers.R
import strathclyde.contextualtriggers.userPersonality.UserPersonalityDecider
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.database.TriggerWithContextConstraints
import strathclyde.contextualtriggers.database.UserPersonalityData
import strathclyde.contextualtriggers.enums.ContextKey
import strathclyde.contextualtriggers.enums.IconKey
import strathclyde.contextualtriggers.enums.PersonalityKey
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class Trigger(
    private val application: Application,
    contexts: List<Context>,
    triggerWithContextConstraints: TriggerWithContextConstraints
) {
    private val owner = triggerWithContextConstraints.trigger.owner
    private val title = triggerWithContextConstraints.trigger.title
    private val content = triggerWithContextConstraints.trigger.content
    private val altContent = triggerWithContextConstraints.trigger.altContent
    private val iconKey = IconKey.valueOf(triggerWithContextConstraints.trigger.iconKey)
    private val contextConstraintsMap: MutableMap<Context, MutableList<Constraint>> = mutableMapOf()
    private val completionKey = triggerWithContextConstraints.trigger.completionKey
    private val completionValue = triggerWithContextConstraints.trigger.completionValue
    private val useProgressBar = triggerWithContextConstraints.trigger.useProgressBar
    private val actionKeys: List<String> =
        triggerWithContextConstraints.trigger.actionKeys.split(",")
    private val actionContentUri = triggerWithContextConstraints.trigger.actionContentUri
    private val progressContentUri = triggerWithContextConstraints.trigger.progressContentUri

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
                context.register(this)
            }
            constraintList.add(
                Constraint(
                    constraint.lessThanOrEqualTo,
                    constraint.greaterThanOrEqualTo
                )
            )
        }
    }

    fun onDestroy() {
        contextConstraintsMap.forEach { (t, _) ->
            t.unregister(this)
        }
    }

    fun update(context: Context, value: Int) {
        contextConstraintsMap[context]?.forEach { constraint ->
            constraint.evaluate(value)
        }

        contextConstraintsMap.forEach { (_, constraints) ->
            if (!constraints.map { constraint -> constraint.state }.reduce { acc, state -> acc || state }) return
        }
        Log.i("Trigger $title", "notification")
        if (completionKey != PersonalityKey.DEFAULT.resolve() && completionValue != 0)
            UserPersonalityDecider.getDecider(application.applicationContext).updatePersonality(completionKey, completionValue)
        createNotification()
    }

    private fun createNotification() {
        val notificiationBuilder = getBaseNotification()

        if (useProgressBar) {
            val (current_val, max_val, indeterminate) = requestProgressBarValues(progressContentUri)
            notificiationBuilder.setProgress(max_val, current_val, indeterminate)
        }
        if (actionKeys.isNotEmpty()) {
            requestActionKeyValues(actionContentUri, actionKeys).forEach {
                notificiationBuilder.addAction(it)
            }
        }
        val notification: Notification =
            if (altContent == "" || with(UserPersonalityDecider) {
                    getDecider(application.applicationContext)
                        .isPositivePersonality()
                }
            ) {
                createDefaultNotification(notificiationBuilder)
            } else {
                createAltNotification(notificiationBuilder)
            }

        notificationManager.notify(R.integer.walkingNotificationId, notification)
    }


    private fun requestActionKeyValues(
        uri: String,
        actionKeys: List<String>
    ): List<Notification.Action> {

        return actionKeys.map {
            val actionIntent = Intent(it)
                .setComponent(ComponentName(owner, uri))
            val pendingActionIntent: PendingIntent =
                PendingIntent.getBroadcast(application, 0, actionIntent, 0)
            Builder(iconKey.resolveResource(), it, pendingActionIntent).build()
        }
    }


    private fun requestProgressBarValues(progressContentUri: String): Triple<Int, Int, Boolean> {
        var result = Triple(0, 100, true)
        val cursor = application.contentResolver.query(Uri.parse(progressContentUri), null, null, null, null)
        cursor?.apply {
            moveToFirst()
            val value = getInt(0)
            val max = getInt(1)
            result = Triple(value, max, max == 0)
            close()
        }
        return result
    }

    private fun getBaseNotification() =
        Notification.Builder(application, application.getString(R.string.channel_id))
            .setContentTitle(title)
            .setSmallIcon(iconKey.resolveResource())

    private fun createDefaultNotification(builder: Notification.Builder): Notification =
        builder.setContentText(content).build()

    private fun createAltNotification(builder: Notification.Builder): Notification =
        builder.setContentText(altContent).build()
}
