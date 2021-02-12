package ru.maxdexter.mytasks.domen.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskFile(
                    @PrimaryKey
                    var uri: String = "",
                    var saveToCloud: Boolean = false,
                    var taskUUID: String = "",
                    var fileType: String = "",
                    var name: String = "")


