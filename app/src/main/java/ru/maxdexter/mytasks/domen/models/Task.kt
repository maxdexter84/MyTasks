package ru.maxdexter.mytasks.domen.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Task(   @PrimaryKey
                   var id: String = UUID.randomUUID().toString(),
                   var title: String = "",
                   var description: String = "",
                   var pushMessage: Boolean = false,
                   var saveToCloud:Boolean = false,
                   var eventYear: Int = 0,
                   var eventMonth: Int = 0,
                   var eventDay: Int = 0,
                   var eventHour: Int = 0,
                   var eventMinute: Int = 0,
                   var isCompleted: Boolean = false,
                   var repeat: Boolean = false,
                   var repeatRange: String = "",
                   var repeatRangeValue: Long = 0L,
                   var userNumber: String = "")