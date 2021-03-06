package ru.maxdexter.mytasks.domen.models

 data class Hour(val startPeriod: Int, val endPeriod: Int, val list: List<TaskWithTaskFile>? = null)
