package ru.maxdexter.mytasks.utils

import ru.maxdexter.mytasks.models.TaskWithTaskFile
import java.lang.StringBuilder
import java.util.*

fun mapDateToLong(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long{
    return GregorianCalendar(year,month,day,hour,minute).timeInMillis
}

 fun handleParseDate(taskWithTaskFile: TaskWithTaskFile):String {
    val day = taskWithTaskFile.task?.eventDay?.let { if (it < 10) "0$it" else "$it"}
    val month = taskWithTaskFile.task?.eventMonth?.let { if (it < 10) "0$it" else "$it"}
    val year = taskWithTaskFile.task?.eventYear
    return "$day $month $year"
}

 fun handleParseTime(taskWithTaskFile: TaskWithTaskFile):String {
    val minute = taskWithTaskFile.task?.eventMinute?.let { if (it < 10) "0$it" else "$it"}
    val hour = taskWithTaskFile.task?.eventHour?.let { if (it < 10) "0$it" else "$it"}
    return  "$hour : $minute"
}


fun handleParseFileName(uri: String): String{
    val arr = if (uri.contains("%2F") )uri.split("%2F") else uri.split("/")
    val strB = StringBuilder()
    strB.append(arr.last())
    return strB.toString()

}