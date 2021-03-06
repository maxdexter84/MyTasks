package ru.maxdexter.mytasks.data.firebase.entity

import com.google.firebase.Timestamp
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import java.util.*


data class TaskFS(
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var description: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    var pushMessage: Boolean = false,
    var completed: Boolean = false,
    var repeat: Boolean = false,
    var repeatRangeValue: Long = 0L,
    var userFilesCloudStorage: List<TaskFile> = emptyList(),
    var userNumber: String = "")
