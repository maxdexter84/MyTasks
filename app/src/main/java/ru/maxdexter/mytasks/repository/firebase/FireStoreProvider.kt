package ru.maxdexter.mytasks.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.User
import ru.maxdexter.mytasks.repository.LoadingResponse
import ru.maxdexter.mytasks.repository.RemoteDataProvider

class FireStoreProvider : RemoteDataProvider{
    override fun saveTask(task: Task): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun deleteTask(task: Task): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun <T> getAllTask(): Flow<LoadingResponse<T>> {
        TODO("Not yet implemented")
    }

    override fun <T> getTaskByUUID(uuid: String): Flow<LoadingResponse<T>> {
        TODO("Not yet implemented")
    }



}