package com.example.booksearchapp.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CacheDeleteWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val cacheDeleteResult: String
) : Worker(context, workerParameters) {

    // doWork() 함수 내부에 백그라운드 작업을 정의한다.
    override fun doWork(): Result {
        return try {
            Log.d("WorkManager", cacheDeleteResult)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
