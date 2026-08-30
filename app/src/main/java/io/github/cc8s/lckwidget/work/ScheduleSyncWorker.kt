package io.github.cc8s.lckwidget.work

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.github.cc8s.lckwidget.data.ScheduleRepository
import io.github.cc8s.lckwidget.widget.LckWidget
import java.util.concurrent.TimeUnit

class ScheduleSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            ScheduleRepository(applicationContext).refresh()
            LckWidget().updateAll(applicationContext)
            Log.d("LCK", "동기화 성공")
            Result.success()
        } catch (e: Exception) {
            Log.w("LCK", "동기화 실패", e)
            if (runAttemptCount < 3) Result.retry() else Result.success()
        }
    }

    companion object {
        private const val UNIQUE_NAME = "lck_schedule_sync"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<ScheduleSyncWorker>(
                6, TimeUnit.HOURS
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}