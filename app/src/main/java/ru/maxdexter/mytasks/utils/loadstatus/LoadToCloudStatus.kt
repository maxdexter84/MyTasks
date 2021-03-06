package ru.maxdexter.mytasks.utils.loadstatus

import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile

sealed class LoadToCloudStatus{
    class Success(val data: List<TaskFile>) : LoadToCloudStatus()
    class Error(val message: String) : LoadToCloudStatus()
    object Loading : LoadToCloudStatus()
}