package strathclyde.contextualtriggers.context.steps

import android.app.Application
import android.net.Uri
import java.util.*

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

    class Day(
        val date: String,
        val steps: Int,
        val goal: String,
        val target: Int
    ) {
        companion object {
            const val dateColumn = 1
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

    private val todayUri = Uri.Builder()
        .scheme("content")
        .authority("strathclyde.emb15144.stepcounter.provider")
        .path("today")
        .build()

    private val historyUri = Uri.Builder()
        .scheme("content")
        .authority("strathclyde.emb15144.stepcounter.provider")
        .path("history")
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

    fun getToday(): Day? {
        var state: Day? = null
        application.contentResolver.query(todayUri, null, null, null, null)?.apply {
            if (moveToFirst()) {
                state = Day(
                    getString(Day.dateColumn),
                    getInt(Day.stepsColumn),
                    getString(Day.goalColumn),
                    getInt(Day.targetColumn)
                )
            }
            close()
        }
        return state
    }

    fun getHistory(): List<Day> {
        val days = mutableListOf<Day>()
        application.contentResolver.query(historyUri, null, null, null, null)?.apply {
            while (moveToNext()) {
                days.add(
                    Day(
                        getString(Day.dateColumn),
                        getInt(Day.stepsColumn),
                        getString(Day.goalColumn),
                        getInt(Day.targetColumn)
                ))
            }
            close()
        }
        return days
    }
}
