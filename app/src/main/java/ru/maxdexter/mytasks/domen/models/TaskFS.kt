package ru.maxdexter.mytasks.domen.models

import com.google.firebase.Timestamp
import java.util.*


data class TaskFS(
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var description: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    var pushMessage: Boolean = false,
    var completed: Boolean = false,
    var repeat: Boolean = false,
    var repeatRange: String = "",
    var repeatRangeValue: Long = 0L,
    var userFilesCloudStorage: List<TaskFile> = emptyList(),
    var userNumber: String = "")
