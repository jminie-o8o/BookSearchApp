package com.stark.booksearchapp.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

@HiltWorker
class CacheDeleteWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        return try {
            clearApplicationCache(applicationContext)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun clearApplicationCache(context: Context) {
        val cache = context.cacheDir
        val appDir = cache.parent?.let { File(it) }
        if (appDir?.exists() == true) {
            val children = appDir.list()
            if (children != null) {
                for (s in children) {
                    if (s.equals("lib") || s.equals("files")) continue
                    deleteDir(File(appDir, s))
                    Log.d("WorkManager", "File/data/data ${context.packageName} / $s Deleted")
                }
            }
        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            if (children != null) {
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
        }
        return dir.delete()
    }
}
