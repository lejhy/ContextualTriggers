package strathclyde.contextualtriggers.database

import androidx.room.Embedded
import androidx.room.Relation

data class TriggerWithContextConstraints(
    @Embedded val trigger: Trigger,
    @Relation(
        parentColumn = "id",
        entityColumn = "triggerId"
    )
    val contextConstraints: List<ContextConstraint>
)
