package ru.maxdexter.mytasks.models

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithTaskFile(
    @Embedded
    var task: Task? = null,
    @Relation(parentColumn = "id", entityColumn = "taskUUID",entity = TaskFile::class,)
    var list: List<TaskFile>? = null) {
}