package ru.maxdexter.mytasks.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class TaskFile(
                    @PrimaryKey
                    var uri: String = "",
                    var taskUUID: String = "",
                    var name: String = "")


