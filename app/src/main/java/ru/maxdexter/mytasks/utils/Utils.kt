package ru.maxdexter.mytasks.utils

import ru.maxdexter.mytasks.models.Task
import java.util.*

fun mapDateToLong(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long{
    return GregorianCalendar(year,month,day,hour,minute).timeInMillis
}

