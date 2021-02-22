package ru.maxdexter.mytasks.domen.repository


import kotlinx.coroutines.flow.StateFlow
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.utils.loadstatus.LoadToCloudStatus


interface DataStorage {
    suspend fun saveFileToStorage(taskFileList: List<TaskFile>, userNumber: String): StateFlow<LoadToCloudStatus>
    suspend fun getFileFromStorage(uri: String, taskFS: TaskFS): LoadingResponse
    suspend fun deleteFileFromStorage(name: String?, taskFS: TaskFS?):  LoadingResponse
}