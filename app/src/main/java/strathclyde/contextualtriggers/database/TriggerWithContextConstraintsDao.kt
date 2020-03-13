package strathclyde.contextualtriggers.database

import androidx.room.*

@Dao
abstract class TriggerWithContextConstraintsDao {
    @Transaction
    @Query("SELECT * FROM `Trigger`")
    abstract fun getAll(): List<TriggerWithContextConstraints>

    fun insertTriggerWithContextConstraints(triggerWithContextConstraints: TriggerWithContextConstraints) {
        val trigger = triggerWithContextConstraints.trigger
        val contextConstraints = triggerWithContextConstraints.contextConstraints

        trigger.id = insertTrigger(trigger)
        contextConstraints.forEach {
            it.triggerId = trigger.id
        }
        insertContextConstraints(contextConstraints)
    }

    @Update
    abstract fun updateTrigger(trigger: Trigger)

    @Insert
    protected abstract fun insertContextConstraints(contextConstraints: List<ContextConstraint>)

    @Insert
    protected abstract fun insertTrigger(trigger: Trigger): Long
}
