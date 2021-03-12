package ru.maxdexter.mytasks.ui.entity

import java.util.*

data class UITask(val id: String = UUID.randomUUID().toString(),
             val title: String = "",
             val description: String = "",
             val pushMessage: Boolean = false,
             val eventYear: Int = 0,
             val eventMonth: Int = 0,
             val eventDay: Int = 0,
             val eventHour: Int = 0,
             val eventMinute: Int = 0,
             val isCompleted: Boolean = false,
             val repeat: Boolean = false,
             val repeatRangeValue: Int = -1,
             val fileList: List<UIFile> = emptyList(),
             val userNumber: String = "")
