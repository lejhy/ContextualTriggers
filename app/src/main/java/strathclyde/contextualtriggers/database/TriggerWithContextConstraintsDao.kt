package strathclyde.contextualtriggers.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class TriggerWithContextConstraintsDao {
    @Transaction
    @Query("SELECT * FROM `Trigger`")
    abstract fun getAll(): List<TriggerWithContextConstraints>

    @Transaction
    @Query("SELECT * FROM `Trigger`")
    abstract fun getAllObservable(): LiveData<List<TriggerWithContextConstraints>>

    fun insertTriggerWithContextConstraints(triggerWithContextConstraints: TriggerWithContextConstraints) {
        val trigger = triggerWithContextConstraints.trigger
        val contextConstraints = triggerWithContextConstraints.contextConstraints

        trigger.id = insertTrigger(trigger)
        contextConstraints.forEach {
            it.triggerId = trigger.id
        }
        insertContextConstraints(contextConstraints)
    }

    @Transaction
    @Query("SELECT * FROM `Trigger` WHERE owner = :owner AND title = :title")
    abstract fun get(owner: String, title: String): TriggerWithContextConstraints?

    fun deleteTriggerWithContextConstraints(triggerWithContextConstraints: TriggerWithContextConstraints): Boolean {
        val trigger = triggerWithContextConstraints.trigger
        val contextConstraints = triggerWithContextConstraints.contextConstraints

        val result = deleteTrigger(trigger)
        deleteContextConstraints(contextConstraints)
        return result > 0
    }

    @Update
    abstract fun updateTrigger(trigger: Trigger)

    @Insert
    protected abstract fun insertContextConstraints(contextConstraints: List<ContextConstraint>)

    @Insert
    protected abstract fun insertTrigger(trigger: Trigger): Long

    @Insert
    abstract fun insertTriggers(trigger: List<Trigger>)

    @Delete
    protected abstract fun deleteTrigger(trigger: Trigger): Int

    @Delete
    protected abstract fun deleteContextConstraints(contextConstraints: List<ContextConstraint>): Int
}
