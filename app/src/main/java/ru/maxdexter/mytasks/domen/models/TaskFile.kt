package ru.maxdexter.mytasks.domen.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskFile(
                    @PrimaryKey
                    var uri: String = "",
                    var taskUUID: String = "",
                    var fileType: String = "",
                    var name: String = "")


