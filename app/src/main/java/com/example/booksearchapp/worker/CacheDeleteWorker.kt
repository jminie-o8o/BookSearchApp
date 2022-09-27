package com.example.booksearchapp.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception

class CacheDeleteWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    // doWork() 함수 내부에 백그라운드 작업을 정의한다.
    override fun doWork(): Result {
        return try {
            Log.d("WorkManager", "캐시가 삭제되었습니다.")
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
