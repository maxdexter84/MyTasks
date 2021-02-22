package ru.maxdexter.mytasks.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxdexter.mytasks.domen.models.Hour
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LoadingResponse
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.loadstatus.LoadToCloudStatus
import ru.maxdexter.mytasks.utils.taskFSToTaskWithTaskFile
import ru.maxdexter.mytasks.utils.taskWithTaskFileToTaskFS
import java.io.IOException
import java.util.*

class CalendarViewModel(private val localDatabase: LocalDatabase, private val remoteDatabase: RemoteDataProvider, private val storage: DataStorage) : ViewModel() {


    private var _listTaskWithTaskFile = MutableLiveData<List<TaskWithTaskFile>>(emptyList())
        val listTaskFile: LiveData<List<TaskWithTaskFile>>
        get() = _listTaskWithTaskFile

    val calendar = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault())
    private var _selectedTask = MutableLiveData("")
            val selectedTask: LiveData<String>
            get() = _selectedTask



    init {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        loadData(year, month, day)
    }

     fun loadData(year: Int, month: Int, day: Int){
         viewModelScope.launch {
             localDatabase.getAllTaskWithTaskFile(year,month,day).collect {
                 _listTaskWithTaskFile.value = it
             }

         }
    }


    fun updateData(
        it: List<TaskWithTaskFile>
    ): MutableList<Hour> {
        val hourList = mutableListOf<Hour>()
        for (i in 0..23 step 1) {
            val taskWithTaskFile = mutableListOf<TaskWithTaskFile>()
            for (item in it) {
                if (item.task.eventHour == i) {
                    taskWithTaskFile.add(item)

                }
            }
            hourList.add(Hour(i, ("$i" + "59").toInt(), taskWithTaskFile))
        }
        return hourList
    }

    fun selectedTask(uuid: String){
        _selectedTask.value = uuid

    }

     fun getAllTaskFromFirestore(){
        viewModelScope.launch {
            remoteDatabase.getAllTask().collect{ loadingResponse ->
                when(loadingResponse){
                    is LoadingResponse.Success<*> -> {
                        val list = loadingResponse.data as List<TaskFS>
                        list.map { taskFSToTaskWithTaskFile(it) }.let {
                            _listTaskWithTaskFile.value = it
                            Log.i("LOAD_DATA", "data load success")
                        }

                    }
                    is LoadingResponse.Error -> {
                        _listTaskWithTaskFile.value = emptyList()
                        Log.i("LOAD_DATA_ERROR", loadingResponse.message)
                    }
                    is LoadingResponse.Loading ->  Log.i("LOAD_DATA_ERROR","some error")
                }
            }
        }
    }




}