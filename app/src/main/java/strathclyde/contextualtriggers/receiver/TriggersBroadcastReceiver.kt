package strathclyde.contextualtriggers.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.*
import strathclyde.contextualtriggers.database.ContextConstraint
import strathclyde.contextualtriggers.database.MainDatabase
import strathclyde.contextualtriggers.database.Trigger
import strathclyde.contextualtriggers.database.TriggerWithContextConstraints

class TriggersBroadcastReceiver : BroadcastReceiver() {

    val newTriggerAction = "strathclyde.contextualtriggers.intent.action.NEW_TRIGGER"
    val updateTriggerAction = "strathclyde.contextualtriggers.intent.action.UPDATE_TRIGGER"
    val deleteTriggerAction = "strathclyde.contextualtriggers.intent.action.DELETE_TRIGGER"

    private var receiverJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + receiverJob)

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            newTriggerAction -> uiScope.launch {
                try {
                    val title = intent.getStringExtra("title")!!
                    val datasource = MainDatabase.getInstance(context)
                    val existingTriggerWithContextConstraints = withContext(Dispatchers.IO) {
                        datasource.triggerWithContextConstraintsDao.get(title)
                    }
                    if (existingTriggerWithContextConstraints == null) {
                        val trigger = Trigger(
                            0L,
                            title,
                            intent.getStringExtra("content")!!,
                            intent.getStringExtra("iconKey")!!,
                            intent.getBooleanExtra("active", true)
                        )
                        if (intent.getBooleanExtra("useProgressBar", false)) {
                            trigger.useProgressBar = true
                            trigger.progressContentUri = intent.getStringExtra("progressContentUri")!!
                        }
                        val actionKeys = intent.getStringExtra("actionKeys")
                        actionKeys?.let {
                            trigger.actionKeys = actionKeys
                            trigger.actionContentUri = intent.getStringExtra("actionContentUri")!!
                        }
                        val contextConstraints = mutableListOf<ContextConstraint>()
                        val contextKeyList = intent.getStringArrayListExtra("contextKeyList")!!
                        val greaterThanOrEqualToList = intent.getIntegerArrayListExtra("greaterThanOrEqualToList")!!
                        val lessThanOrEqualToToList = intent.getIntegerArrayListExtra("lessThanOrEqualToList")!!
                        for ((index, contextKey) in contextKeyList.withIndex()) {
                            contextConstraints.add(
                                ContextConstraint(
                                    0L,
                                    contextKey,
                                    greaterThanOrEqualToList[index],
                                    lessThanOrEqualToToList[index],
                                    0L
                                )
                            )
                        }
                        val triggerWithContextConstraints = TriggerWithContextConstraints(
                            trigger,
                            contextConstraints
                        )
                        withContext(Dispatchers.IO) {
                            datasource.triggerWithContextConstraintsDao.insertTriggerWithContextConstraints(
                                triggerWithContextConstraints
                            )
                            datasource.triggerWithContextConstraintsDao.get(trigger.title)?.let {
                                Log.i(it.trigger.title, it.trigger.content)
                            }
                        }
                        Log.i("TriggersBroadcastReceiver", "New trigger created...")
                    } else {
                        Log.i("TriggersBroadcastReceiver", "Trigger already exists...")
                    }
                } catch (e: NullPointerException) {
                    Log.i("TriggersBroadcastReceiver", "Failed to create trigger...")
                }
            }
            updateTriggerAction -> uiScope.launch {
                //TODO
            }
            deleteTriggerAction -> uiScope.launch {
                try {
                    val title = intent.getStringExtra("title")!!
                    val datasource = MainDatabase.getInstance(context)
                    withContext(Dispatchers.IO) {
                        val triggerWithContextConstraints = datasource.triggerWithContextConstraintsDao.get(title)!!
                        datasource.triggerWithContextConstraintsDao.deleteTriggerWithContextConstraints(
                            triggerWithContextConstraints
                        )
                    }
                } catch (e: NullPointerException) {
                    Log.i("TriggersBroadcastReceiver", "Failed to delete trigger...")
                }
            }
        }
    }
}
