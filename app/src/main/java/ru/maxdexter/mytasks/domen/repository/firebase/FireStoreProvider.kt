package ru.maxdexter.mytasks.domen.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.repository.LoadingResponse
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.TASKS_COLLECTION
import ru.maxdexter.mytasks.utils.USERS_COLLECTION


@KoinApiExtension
class FireStoreProvider(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth) : RemoteDataProvider{

    private val currentUser
        get() = firebaseAuth.currentUser

    override fun getUserTasksCollection() = currentUser?.let {
        firestore.collection(USERS_COLLECTION).document(it.uid).collection(TASKS_COLLECTION)
    }?: throw Exception("Not Auth Exception")


    override fun saveAllTasks(tasks: List<Task>): Flow<Boolean> {
        TODO("Not yet implemented")
    }

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