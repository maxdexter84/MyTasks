package ru.maxdexter.mytasks.ui.entity

import ru.maxdexter.mytasks.data.localdatabase.entity.TaskWithTaskFile

data class Hour(val startPeriod: Int, val endPeriod: Int, val list: List<TaskWithTaskFile>? = null)
