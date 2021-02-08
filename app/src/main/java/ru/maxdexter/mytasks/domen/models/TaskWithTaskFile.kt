package ru.maxdexter.mytasks.domen.models

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithTaskFile(
    @Embedded
    var task: Task = Task(),
    @Relation(parentColumn = "id", entityColumn = "taskUUID",entity = TaskFile::class,)
    var list: List<TaskFile> = emptyList()) {
}