package strathclyde.contextualtriggers.broadcasters

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import strathclyde.contextualtriggers.context.steps.KeepFitContentGetter

class HistoryJobService : JobService() {
    companion object {
        const val HISTORY_BROADCAST_ID = "strathclyde.contextualtriggers.broadcast.HISTORY"
    }

    override fun onStartJob(params: JobParameters): Boolean {
        doBackgroundWork(params)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("JOB CANCELLED", "History Job before complete")
        return true
    }

    private fun doBackgroundWork(jobParameters: JobParameters) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("History Coroutine", "Stated")
            fetchHistory(
                KeepFitContentGetter.WEEK_PROGRESS_PROVIDER,
                KeepFitContentGetter.GOAL_PROGRESS_PROVIDER,
                7
            )
            jobFinished(jobParameters, false)
        }
    }

    private fun fetchHistory(uri: Uri, uriGoals: Uri, numberOfDays: Int) {
        val days = mutableListOf<KeepFitContentGetter.Day>()
        application.contentResolver.query(uri, null, null, null, null)?.apply {
            while (moveToNext()) {
                days.add(
                    KeepFitContentGetter.Day(
                        getString(KeepFitContentGetter.Day.dateColumn),
                        getInt(KeepFitContentGetter.Day.stepsColumn),
                        getString(KeepFitContentGetter.Day.goalColumn),
                        getInt(KeepFitContentGetter.Day.targetColumn)
                    )
                )
            }
            close()
        }

        var nextGoal: KeepFitContentGetter.Goal =
            KeepFitContentGetter.Goal(days.last().goal, days.last().steps)

        application.contentResolver.query(uriGoals, null, null, null, null)?.apply {
            moveToFirst()
            nextGoal = KeepFitContentGetter.Goal(
                getString(1),
                getInt(2)
            )
        }

        broadcastHistory(
            numberOfDays,
            days.sortedBy { day -> day.date }.takeLast(numberOfDays),
            nextGoal
        )
    }

    private fun broadcastHistory(
        numberOfDays: Int,
        history: List<KeepFitContentGetter.Day>,
        nextGoal: KeepFitContentGetter.Goal
    ) {
        Log.i("BroadcastHistory", "Create intent for history, $numberOfDays")
        val intent = Intent().also {
            it.action = HISTORY_BROADCAST_ID
            it.putExtra("days", numberOfDays.toDouble())
            it.putExtra("next_largest_goal_name", nextGoal.name)
            it.putExtra("next_largest_goal_steps", nextGoal.steps)
            it.putExtra("steps", history.map { day -> day.steps }.sum().toDouble())
            it.putExtra("goal", history.map { day -> day.target }.sum().toDouble())
            it.putExtra("day_steps", history.last().steps)
            it.putExtra("day_target", history.last().target)
            it.putExtra("day_goal", history.last().goal)
            it.putExtra("completed", history.map { day ->
                when {
                    day.steps >= day.target -> 1
                    else -> 0
                }.toDouble()
            }.sum())
        }
        Log.i("BroadcastHistory", "Broadcast Intent")
        application.sendBroadcast(intent)
    }
}