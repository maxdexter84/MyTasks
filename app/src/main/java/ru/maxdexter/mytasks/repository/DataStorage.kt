package ru.maxdexter.mytasks.repository


import kotlinx.coroutines.flow.StateFlow
import ru.maxdexter.mytasks.data.firebase.entity.TaskFS
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import ru.maxdexter.mytasks.utils.loadstatus.LoadToCloudStatus


interface DataStorage {
    suspend fun saveFileToStorage(taskFileList: List<TaskFile>, userNumber: String): StateFlow<LoadToCloudStatus>
    suspend fun getFileFromStorage(uri: String, taskFS: TaskFS): LoadingResponse
    suspend fun deleteFileFromStorage(name: String?, taskFS: TaskFS?):  LoadingResponse
}