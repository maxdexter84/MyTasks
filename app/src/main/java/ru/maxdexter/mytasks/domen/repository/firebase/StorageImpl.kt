package ru.maxdexter.mytasks.domen.repository.firebase

import android.app.Application
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LoadingResponse
import ru.maxdexter.mytasks.utils.loadstatus.LoadToCloudStatus
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class StorageImpl(private val storage: FirebaseStorage, private val application: Application) : DataStorage {
    private val storageRef = storage.reference
  override suspend fun saveFileToStorage(taskFileList: List<TaskFile>,userNumber: String): StateFlow<LoadToCloudStatus>{
      val stateFlow = MutableStateFlow<LoadToCloudStatus>(LoadToCloudStatus.Loading)
           try {
               val userStorage = userNumber.let { storageRef.child(it) }
               val resultList = mutableListOf<TaskFile>()
               var count = 0
               taskFileList.forEach{item->
                  val resUri = saveFile(userStorage,item).await()
                       .task.addOnCompleteListener { task->
                           if (task.isComplete && task.isSuccessful) {
                               val taskFile: TaskFile = item.copy()
                               taskFile.saveToCloud = true
                               taskFile.name = task.result?.metadata?.name.toString()
                               taskFile.fileType = task.result?.metadata?.contentType.toString()
                               task.result?.storage?.downloadUrl?.addOnSuccessListener {
                                   taskFile.uri = it.toString()
                                   resultList.add(taskFile)
                               }
                               count++
                           }
                       }
               }

               stateFlow.value = LoadToCloudStatus.Success(resultList)
           }catch (e: Exception){
               stateFlow.value = LoadToCloudStatus.Error(e.message ?: "")
               Log.e("UPLOAD_ERROR",e.message.toString())
           }
    return stateFlow
   }

    private  fun saveFile(storageReference: StorageReference, taskFile: TaskFile): UploadTask {
        return storageReference.child(taskFile.name).putFile(taskFile.uri.toUri())
    }


    override suspend fun getFileFromStorage(uri: String, taskFS: TaskFS): LoadingResponse = suspendCoroutine { continuation ->
        val storageRef = storage.reference
        continuation.resume(LoadingResponse.Loading)
        val userStorage = taskFS.userNumber.let { storageRef.child(it) }
        val list:MutableList<TaskFile> = mutableListOf()
        try {
            taskFS.userFilesCloudStorage.forEach {taskFile ->
                val pref = taskFile.name.split(".")[0]
                val suffix = taskFile.name.split(".")[1]
                val localFile = File.createTempFile(pref,suffix, application.filesDir)
                val userFile = userStorage.getFile(localFile)
                list.add(TaskFile(uri = localFile.absolutePath,saveToCloud = true,taskUUID = taskFS.id,taskFile.fileType,taskFile.name))

            }
            continuation.resume(LoadingResponse.Success(list))
        }catch (e: Exception) {
            e.message?.let { LoadingResponse.Error(it) }?.let { continuation.resume(it) }
        }

    }

    override suspend fun deleteFileFromStorage(name: String?, taskFS: TaskFS?): LoadingResponse = suspendCoroutine {
        val storageRef = storage.reference
        val userStorage = taskFS?.userNumber?.let { storageRef.child(it) }
        if (name != null){
            val file = userStorage?.child(name)
            file?.delete()
        }
        if (taskFS != null){
            taskFS.userFilesCloudStorage.forEach {
                val file = userStorage?.child(it.name)
                file?.delete()
            }
        }


    }


}