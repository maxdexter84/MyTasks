package ru.maxdexter.mytasks.domen.extension

import ru.maxdexter.mytasks.data.localdatabase.entity.Task
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskWithTaskFile
import ru.maxdexter.mytasks.ui.entity.UIFile
import ru.maxdexter.mytasks.ui.entity.UITask
import ru.maxdexter.mytasks.utils.TimeRange


fun TaskWithTaskFile.mapToUITask(): UITask{
    return UITask(task.id,
       title = task.title,
       description = task.description,
       pushMessage = task.pushMessage,
       eventYear =  task.eventYear,
        eventMonth = task.eventMonth,
       eventDay =  task.eventDay,
       eventHour =  task.eventHour,
        eventMinute = task.eventMinute,
       repeat =  task.repeat,
       repeatRangeValue =  mapRangeValueToInt(task.repeatRangeValue),
        fileList = this.list.map { it.mapToUIFile() },
        userNumber = task.userNumber
    )
}

fun TaskFile.mapToUIFile():UIFile{
    return UIFile(uri, saveToCloud, taskUUID, fileType, name)
}

fun UITask.mapToTaskWithTaskFile(): TaskWithTaskFile{
    val task = Task(
        id = this.id,
        title = this.title,
        description = this.description,
        pushMessage = this.pushMessage,
        eventYear =  this.eventYear,
        eventMonth = this.eventMonth,
        eventDay =  this.eventDay,
        eventHour =  this.eventHour,
        eventMinute = this.eventMinute,
        repeat =  this.repeat,
        repeatRangeValue =  mapRangeValueToLong(this.repeatRangeValue),
        userNumber = this.userNumber
    )
    val taskFileList = this.fileList.map { it.mapToTaskFile() }
    return TaskWithTaskFile(task,taskFileList)
}

fun UIFile.mapToTaskFile(): TaskFile{
    return TaskFile(
        uri, saveToCloud, taskUUID, fileType, name
    )
}


fun mapRangeValueToLong(range: Int = -1) : Long{
    return when(range){
        TimeRange.HOUR.key -> TimeRange.HOUR.value
        TimeRange.DAY.key -> TimeRange.DAY.value
        TimeRange.MONTH.key -> TimeRange.MONTH.value
        TimeRange.YEAR.key -> TimeRange.YEAR.value
        else -> 0L
    }
}

fun mapRangeValueToInt(range: Long = 0) : Int{
    return when(range){
        TimeRange.HOUR.value -> TimeRange.HOUR.key
        TimeRange.DAY.value -> TimeRange.DAY.key
        TimeRange.MONTH.value -> TimeRange.MONTH.key
        TimeRange.YEAR.value -> TimeRange.YEAR.key
        else -> -1
    }
}