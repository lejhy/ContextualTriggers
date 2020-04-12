package strathclyde.contextualtriggers.context.steps

import android.app.Application
import android.net.Uri

class KeepFitContentGetter(val application: Application) {

    class Goal(
        val name: String,
        val steps: Int
    ) {
        companion object {
            const val nameColumn = 1
            const val stepsColumn = 2
        }
    }

    class State(
        val steps: Int,
        val goal: String,
        val target: Int
    ) {
        companion object {
            const val stepsColumn = 2
            const val goalColumn = 4
            const val targetColumn = 5
        }
    }

    private val goalsUri = Uri.Builder()
        .scheme("content")
        .authority("strathclyde.emb15144.stepcounter.provider")
        .path("goals")
        .build()

    private val stateUri = Uri.Builder()
        .scheme("content")
        .authority("strathclyde.emb15144.stepcounter.provider")
        .path("state")
        .build()

    fun getGoals(): List<Goal> {
        val goals = mutableListOf<Goal>()
        application.contentResolver.query(goalsUri, null, null, null, null)?.apply {
            while (moveToNext()) {
                goals.add(Goal(
                    getString(Goal.nameColumn),
                    getInt(Goal.stepsColumn)
                ))
            }
            close()
        }
        return goals
    }

    fun getState(): State? {
        var state: State? = null
        application.contentResolver.query(stateUri, null, null, null, null)?.apply {
            if (moveToFirst()) {
                state = State(
                    getInt(State.stepsColumn),
                    getString(State.goalColumn),
                    getInt(State.targetColumn)
                )
            }
            close()
        }
        return state
    }
}
