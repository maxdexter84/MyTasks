package ru.maxdexter.mytasks.domen.repository.firebase

import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.repository.LoadingResponse
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider

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