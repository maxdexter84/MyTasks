package ru.maxdexter.mytasks.domen.repository

import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import java.io.File
import java.io.InputStream
import kotlin.coroutines.suspendCoroutine

interface DataStorage {
    fun saveFileToStorage(taskWithTaskFile: TaskWithTaskFile): StateFlow<LoadingResponse>
    suspend fun getFileFromStorage(uri: String, taskFS: TaskFS): LoadingResponse
    suspend fun deleteFileFromStorage(name: String?, taskFS: TaskFS?):  LoadingResponse
}