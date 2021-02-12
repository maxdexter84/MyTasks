package ru.maxdexter.mytasks.domen.repository.firebase

import android.app.Application
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.maxdexter.mytasks.di.application
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LoadingResponse
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class StorageImpl(private val storage: FirebaseStorage, private val application: Application) : DataStorage {
    private val storageRef = storage.reference


    override  fun saveFileToStorage(taskWithTaskFile: TaskWithTaskFile): StateFlow<LoadingResponse> {
        val stateFlow = MutableStateFlow<LoadingResponse>(LoadingResponse.Loading)
        try {
            val userStorage = taskWithTaskFile.task.userNumber.let { storageRef.child(it) }
            taskWithTaskFile.list.forEach { taskFile ->
                userStorage.child(taskFile.name)
                    .putFile(taskFile.uri.toUri()).addOnSuccessListener {
                        stateFlow.value = LoadingResponse.Success(it.storage.downloadUrl, true)
                    }.addOnFailureListener {
                        stateFlow.value = LoadingResponse.Error(it.message ?: "",false)
                    }

            }
        }catch (e: Exception){
            stateFlow.value = LoadingResponse.Error(e.message ?: "",false)
        }
       return stateFlow
    }


    override suspend fun getFileFromStorage(uri: String, taskFS: TaskFS): LoadingResponse = suspendCoroutine { continuation ->
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
            continuation.resume(LoadingResponse.Success(list,true))
        }catch (e: Exception) {
            e.message?.let { LoadingResponse.Error(it,false) }?.let { continuation.resume(it) }
        }

    }

    override suspend fun deleteFileFromStorage(name: String?, taskFS: TaskFS?): LoadingResponse = suspendCoroutine {
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