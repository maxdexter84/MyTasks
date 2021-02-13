package ru.maxdexter.mytasks.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import ru.maxdexter.mytasks.domen.repository.DataStorage
import org.koin.core.component.get
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.LoadingResponse
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.taskWithTaskFileToTaskFS
import java.io.IOException


@KoinApiExtension  @Suppress("unchecked_cast")
class WorkerUpload(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), KoinComponent {
    val job = SupervisorJob()
    val scope = CoroutineScope(job + Dispatchers.IO)
    private val storage: DataStorage = get()
    private val remoteRepository: RemoteDataProvider = get()
    override fun doWork(): Result {
        val taskWTF = inputData.keyValueMap.getValue(TASK_WTF) as TaskWithTaskFile
          return try {
                if (!taskWTF.list.isNullOrEmpty()){
                    val result = storage.saveFileToStorage(taskWTF)
//                    when(result){
//                        is LoadingResponse.Loading -> Log.i("SAVED_TO_FIREBASE","start upload and save task to firebase")
//                        is LoadingResponse.Success<*> -> {
//                            taskWTF.list = result.data as MutableList<TaskFile>
//                            saveData(taskWTF)
//                        }
//                    }
                } else {
                   saveData(taskWTF)
                }
                Result.success()
            }catch (e: IOException){
                Result.failure()
            }
    }

    private fun saveData(taskWTF: TaskWithTaskFile){
        scope.launch {
            remoteRepository.saveTask(taskWithTaskFileToTaskFS(taskWTF))
        }
    }




    companion object{
        const val TASK_WTF = "skdlmaflsnd"
        @SuppressLint("RestrictedApi")
        fun startWork(taskWTF: TaskWithTaskFile) = run {
            val task = workDataOf(TASK_WTF to taskWTF)
            val data = Data.Builder().putAll(task).build()
             val workerRequest = OneTimeWorkRequest
                .Builder(WorkerUpload::class.java)
                .setInputData(data)
                .build()

            val workManager = WorkManager.getInstance()
            workManager.enqueue(workerRequest)
            workManager.getWorkInfoByIdLiveData(workerRequest.id)
        }
    }
}