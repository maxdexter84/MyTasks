package ru.maxdexter.mytasks.utils

import com.google.firebase.Timestamp
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import java.lang.StringBuilder
import java.util.*

fun mapDateToLong(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long{
    return GregorianCalendar(year,month,day,hour,minute).timeInMillis
}

 fun handleParseDate(taskWithTaskFile: TaskWithTaskFile):String {
    val day = taskWithTaskFile.task.eventDay.let { if (it < 10) "0$it" else "$it"}
    val month = taskWithTaskFile.task.eventMonth.let { if (it < 10) "0$it" else "$it"}
    val year = taskWithTaskFile.task.eventYear
    return "$day $month $year"
}

 fun handleParseTime(taskWithTaskFile: TaskWithTaskFile):String {
    val minute = taskWithTaskFile.task.eventMinute.let { if (it < 10) "0$it" else "$it"}
    val hour = taskWithTaskFile.task.eventHour.let { if (it < 10) "0$it" else "$it"}
    return  "$hour : $minute"
}


fun handleParseFileName(uri: String): String{
    val arr = if (uri.contains("%2F") )uri.split("%2F") else uri.split("/")
    val strB = StringBuilder()
    strB.append(arr.last())
    return strB.toString()

}


fun taskWithTaskFileToTaskFS(taskWithTaskFile: TaskWithTaskFile): TaskFS {
    val task = taskWithTaskFile.task
    val date: Date = GregorianCalendar(task.eventYear,task.eventMonth,task.eventDay,task.eventHour,task.eventMinute).time
    val id: String = task.id
    val title: String = task.title
    val description: String = task.description
    val timestamp = Timestamp(date)
    val isCompleted: Boolean = task.isCompleted
    val repeat: Boolean = task.repeat
    val repeatRange: String = task.repeatRange
    val repeatRangeValue: Long = task.repeatRangeValue
    val userFiles: List<TaskFile> = taskWithTaskFile.list
    val userNumber: String? = task.userNumber
    return TaskFS(id, title, description, timestamp, isCompleted, repeat, repeatRange, repeatRangeValue, userFiles, userNumber)
}

fun taskFSToTaskWithTaskFile(taskFS: TaskFS): TaskWithTaskFile {
    val calendar = Calendar.getInstance()
    calendar.time = taskFS.timestamp.toDate()
    val id: String = taskFS.id
    val title: String = taskFS.title
    val description: String = taskFS.description
    val eventYear: Int = calendar.get(Calendar.YEAR)
    val eventMonth: Int = calendar.get(Calendar.MONTH)
    val eventDay: Int = calendar.get(Calendar.DAY_OF_WEEK)
    val eventHour: Int = calendar.get(Calendar.HOUR_OF_DAY)
    val eventMinute: Int = calendar.get(Calendar.MINUTE)
    val isCompleted: Boolean = taskFS.isCompleted
    val repeat: Boolean = taskFS.repeat
    val repeatRange: String = taskFS.repeatRange
    val repeatRangeValue: Long = taskFS.repeatRangeValue
    val userNumber: String? = taskFS.userNumber
    val task = Task(id, title, description, eventYear, eventMonth, eventDay, eventHour, eventMinute, isCompleted, repeat, repeatRange, repeatRangeValue, userNumber)
    return TaskWithTaskFile(task,taskFS.userFiles)

}


