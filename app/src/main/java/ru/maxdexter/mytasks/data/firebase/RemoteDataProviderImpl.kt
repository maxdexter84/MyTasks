package ru.maxdexter.mytasks.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.data.firebase.entity.TaskFS
import ru.maxdexter.mytasks.data.firebase.entity.User
import ru.maxdexter.mytasks.repository.LoadingResponse
import ru.maxdexter.mytasks.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.TASKS_COLLECTION
import ru.maxdexter.mytasks.utils.USERS_COLLECTION
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@KoinApiExtension
class RemoteDataProviderImpl(private val firestore: FirebaseFirestore, private val firebaseAuth: FirebaseAuth) : RemoteDataProvider{

    private val currentUser
        get() = firebaseAuth.currentUser

    override fun getUserTasksCollection() = currentUser?.let {
        firestore.collection(USERS_COLLECTION).document(it.uid).collection(TASKS_COLLECTION)
    }?: throw Exception("Not Auth Exception")


    override suspend fun saveAllTasks(tasks: List<TaskFS>): LoadingResponse = suspendCoroutine{ continuation ->
            try {
                tasks.forEach {
                    getUserTasksCollection().document(it.id).set(tasks)
                }
                continuation.resume(LoadingResponse.Success("the data is synchronized"))
            }catch (e: IOException){continuation.resumeWithException(e)}

    }

    override suspend fun  saveTask(task: TaskFS) {
        withContext(Dispatchers.IO){
            try {
                getUserTasksCollection().document(task.id).set(task).addOnFailureListener {
                    it.message?.let { it1 -> Log.e("SAVE_TASK_TO_FIRESTORE", it1) }
                }
            }catch (e: IOException){
                e.message?.let { Log.e("SAVE_TASK_TO_FIRESTORE", it) }
            }
        }


    }

    override suspend fun  deleteTask(task: TaskFS): LoadingResponse = suspendCoroutine{ continuation ->
        try {
            getUserTasksCollection().document(task.id).delete().addOnSuccessListener {
                continuation.resume(LoadingResponse.Success(task))
            }.addOnFailureListener {
                continuation.resume(LoadingResponse.Success(it.message))
            }
        }catch (e: IOException){continuation.resumeWithException(e)}

    }


    @ExperimentalCoroutinesApi
    override suspend fun  getAllTask(): MutableStateFlow<LoadingResponse>  {
        val stateFlow = MutableStateFlow<LoadingResponse>(LoadingResponse.Loading)
        getUserTasksCollection().addSnapshotListener { value, error ->
            if (value != null) {
                val list = value.documents.map { it.toObject(TaskFS::class.java) }
                stateFlow.value = LoadingResponse.Success(list)
            } else {
                stateFlow.value = LoadingResponse.Error(error?.message.toString())
            }

        }
        return stateFlow
    }

    override suspend fun  getTaskByUUID(uuid: String): LoadingResponse = suspendCoroutine{ continuation ->
        try {
           getUserTasksCollection().document(uuid).get().addOnSuccessListener {
               val res = it.toObject(TaskFS::class.java)
               continuation.resume(LoadingResponse.Success(res))
           }.addOnFailureListener {
               continuation.resume(LoadingResponse.Success(it.message))
           }
        }catch (e: IOException){}
    }

     override fun getCurrentUser(): User? =
             currentUser?.let { firebaseUser ->
                if(firebaseUser.phoneNumber.isNullOrEmpty()) null else firebaseUser.phoneNumber?.let {
                    User(it)
                }
            }

}