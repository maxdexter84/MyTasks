package ru.maxdexter.mytasks.domen.repository.firebase

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import ru.maxdexter.mytasks.domen.repository.DataStorage
import java.io.File
import java.io.InputStream

class StorageImpl(private val storage: FirebaseStorage) : DataStorage {
    override fun saveFileToStorage(stream: InputStream): UploadTask {
        TODO("Not yet implemented")
    }

    override fun getFileFromStorage(uploadTask: UploadTask): File {
        TODO("Not yet implemented")
    }

    override fun deleteFileFromStorage(uploadTask: UploadTask) {
        TODO("Not yet implemented")
    }
}