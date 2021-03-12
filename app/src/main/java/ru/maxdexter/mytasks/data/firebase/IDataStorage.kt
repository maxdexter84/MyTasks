package ru.maxdexter.mytasks.data.firebase


import kotlinx.coroutines.flow.StateFlow
import ru.maxdexter.mytasks.data.firebase.entity.TaskFS
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import ru.maxdexter.mytasks.domen.common.LoadingResponse
import ru.maxdexter.mytasks.utils.loadstatus.LoadToCloudStatus


interface IDataStorage {
    suspend fun saveFileToStorage(taskFileList: List<TaskFile>, userNumber: String): StateFlow<LoadToCloudStatus>
    suspend fun getFileFromStorage(uri: String, taskFS: TaskFS): LoadingResponse
    suspend fun deleteFileFromStorage(name: String?, taskFS: TaskFS?): LoadingResponse
}