package ru.maxdexter.mytasks.domen.repository.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.repository.LoadingResponse
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.TASKS_COLLECTION
import ru.maxdexter.mytasks.utils.USERS_COLLECTION
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@KoinApiExtension
class FireStoreProvider(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth) : RemoteDataProvider{

    private val currentUser
        get() = firebaseAuth.currentUser

    override fun getUserTasksCollection() = currentUser?.let {
        firestore.collection(USERS_COLLECTION).document(it.uid).collection(TASKS_COLLECTION)
    }?: throw Exception("Not Auth Exception")


    override suspend fun <T> saveAllTasks(tasks: List<TaskFS>): LoadingResponse<T> = suspendCoroutine{ continuation ->
            try {
                tasks.forEach {
                    getUserTasksCollection().document(it.id).set(tasks)
                }
                continuation.resume(LoadingResponse.Success("the data is synchronized" as T, false))
            }catch (e: IOException){continuation.resumeWithException(e)}

    }

    override suspend fun <T> saveTask(task: TaskFS): LoadingResponse<T> = suspendCoroutine {continuation ->
        try {
            getUserTasksCollection().document(task.id).set(task).addOnSuccessListener {
                continuation.resume(LoadingResponse.Success(task as T,true))
            }.addOnFailureListener {
                continuation.resume(LoadingResponse.Error(it.message as T,false))

            }
        }catch (e: java.lang.Exception){continuation.resumeWithException(e)}

    }

    override suspend fun <T> deleteTask(task: TaskFS): LoadingResponse<T> = suspendCoroutine{ continuation ->
        try {
            getUserTasksCollection().document(task.id).delete().addOnSuccessListener {
                continuation.resume(LoadingResponse.Success(task as T,true))
            }.addOnFailureListener {
                continuation.resume(LoadingResponse.Success(it.message as T,false))
            }
        }catch (e: IOException){continuation.resumeWithException(e)}

    }

    override suspend fun <T> getAllTask(): Flow<LoadingResponse<T>> = callbackFlow {
        getUserTasksCollection().addSnapshotListener { value, error ->
            if (value != null)
            if (!value.isEmpty && error?.localizedMessage.isNullOrEmpty()){

                offer(LoadingResponse.Success(value.documents as T, true))
            }else {
                offer(LoadingResponse.Success(value.documents as T, true))
                if (error != null) {
                    Log.i("FIRESTORE_ALL_TASKS_ERROR", error.localizedMessage)}
                }


        }
    }

    override suspend fun <T> getTaskByUUID(uuid: String): LoadingResponse<T> = suspendCoroutine{ continuation ->
        try {
           getUserTasksCollection().document(uuid).get().addOnSuccessListener {
               val res = it.toObject(TaskFS::class.java)
               continuation.resume(LoadingResponse.Success(res as T,true))
           }.addOnFailureListener {
               continuation.resume(LoadingResponse.Success(it.message as T,true))
           }
        }catch (e: IOException){}
    }


}