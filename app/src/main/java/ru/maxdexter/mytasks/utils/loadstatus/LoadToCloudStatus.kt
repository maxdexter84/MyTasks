package ru.maxdexter.mytasks.utils.loadstatus

import ru.maxdexter.mytasks.domen.models.TaskFile

sealed class LoadToCloudStatus{
    class Success(val data: List<TaskFile>) : LoadToCloudStatus()
    class Error(val message: String) : LoadToCloudStatus()
    object Loading : LoadToCloudStatus()
}