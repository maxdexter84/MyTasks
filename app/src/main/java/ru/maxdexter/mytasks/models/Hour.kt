package ru.maxdexter.mytasks.models

 data class Hour(val startPeriod: Int, val endPeriod: Int, val list: List<TaskWithTaskFile>? = null)
