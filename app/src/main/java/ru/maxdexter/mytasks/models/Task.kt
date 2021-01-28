package ru.maxdexter.mytasks.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Task(   @PrimaryKey
                   var id: String = UUID.randomUUID().toString(),
                   var title: String? = null,
                   var description: String? = null,
                   var eventYear: Int? = null,
                   var eventMonth: Int? = null,
                   var eventDay: Int? = null,
                   var eventHour: Int? = null,
                   var eventMinute: Int? = null,
                   var isCompleted: Boolean = false,
                   var repeat: Boolean = false,
                   var userNumber: String? = null) {

}