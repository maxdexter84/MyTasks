package ru.maxdexter.mytasks.workmanager

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        (1..1000).forEach {
            Thread.sleep(100)
            Log.i("WORKER", it.toString())
        }
        return Result.success()
    }

    companion object{
        val workerRequest = OneTimeWorkRequest.Builder(MyWorker::class.java).build()

        fun startWork() = run {
            val workManager = WorkManager.getInstance()
            workManager.enqueue(workerRequest)
            workManager.getWorkInfoByIdLiveData(workerRequest.id)
        }
    }
}