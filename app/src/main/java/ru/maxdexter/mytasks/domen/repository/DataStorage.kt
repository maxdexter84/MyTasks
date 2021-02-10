package ru.maxdexter.mytasks.domen.repository

import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.InputStream

interface DataStorage {
    fun saveFileToStorage(stream: InputStream): UploadTask
    fun getFileFromStorage(uploadTask: UploadTask): File
    fun deleteFileFromStorage(uploadTask: UploadTask)
}