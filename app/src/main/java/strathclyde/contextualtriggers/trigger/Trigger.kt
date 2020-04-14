package strathclyde.contextualtriggers.trigger

import android.app.*
import android.app.Notification.Action.Builder
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.util.Log
import strathclyde.contextualtriggers.UserPersonality.UserPersonalityDecider
import strathclyde.contextualtriggers.context.Context
import strathclyde.contextualtriggers.database.TriggerWithContextConstraints
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
    private val id = triggerWithContextConstraints.trigger.id
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

        if (!contextConstraintsMap.all { it.value.any { x -> x.state } }) {
            Log.i("Trigger $title", "NO notification with $value in ${context.javaClass}")
            return
        }


        Log.i("Trigger $title", "notification")
        if (completionKey != PersonalityKey.DEFAULT.resolve() && completionValue != 0)
            UserPersonalityDecider.getDecider(application.applicationContext).updatePersonality(
                completionKey,
                completionValue
            )
        createNotification()
    }

    private fun createNotification() {
        val notificationBuilder = getBaseNotification()
        if (useProgressBar) {
            val (current_val, max_val, indeterminate) = requestProgressBarValues(progressContentUri)
            notificationBuilder.setProgress(max_val, current_val, indeterminate)
            notificationBuilder.setSubText("$current_val / $max_val")
        }
        if (actionKeys.isNotEmpty()) {
            requestActionKeyValues(actionContentUri, actionKeys).forEach {
                notificationBuilder.addAction(it)
            }
        }

        if (content == "") {
            Log.d("CreateNotification", "Content is null")
        }
        val notification: Notification =
            if (altContent == "" || UserPersonalityDecider.getDecider(application.applicationContext).isPositivePersonality()) {
                createDefaultNotification(notificationBuilder)
            } else {
                createAltNotification(notificationBuilder)
            }

        notificationManager.createNotificationChannel(
            NotificationChannel(
                owner,
                title,
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        notificationManager.notify(id.toInt(), notification)
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
        val cursor =
            application.contentResolver.query(Uri.parse(progressContentUri), null, null, null, null)
                ?: return result
        var c = cursor.count


        if (c > 7) {
            cursor.moveToPosition(c - 7)
            c = 7
        }
        cursor.apply {
            moveToFirst()

            val stepsColumn =
                if (columnNames.indexOf("value") == -1) columnNames.indexOf("steps") else columnNames.indexOf(
                    "value"
                )
            val targetColumn =
                if (columnNames.indexOf("max") == -1) columnNames.indexOf("goal_steps") else columnNames.indexOf(
                    "max"
                )

            if (columnNames.indexOf("steps") == -1) {
                var steps = getInt(stepsColumn)
                var target = getInt(targetColumn)
                while (--c > 0) {
                    moveToNext()
                    steps += getInt(stepsColumn)
                    target += getInt(targetColumn)
                }
                result = Triple(steps, target, false)
            } else {
                var steps = if (getInt(stepsColumn) >= getInt(targetColumn)) 1 else 0
                var target = 1
                while (--c > 0) {
                    moveToNext()
                    steps += if (getInt(stepsColumn) >= getInt(targetColumn)) 1 else 0
                    target += 1
                }
                result = Triple(steps, target, false)
            }
            close()
        }
        return result

    }

    private fun getBaseNotification() =
        Notification.Builder(application, owner)
            .setContentTitle(title)
            .setGroup(owner)
            .setChannelId(owner)
            .setStyle(Notification.BigTextStyle())
            .setSmallIcon(iconKey.resolveResource())

    private fun createDefaultNotification(builder: Notification.Builder): Notification =
        builder.setContentText(content).build()

    private fun createAltNotification(builder: Notification.Builder): Notification =
        builder.setContentText(altContent).build()
}
