package ru.maxdexter.mytasks.utils


enum class TimeRange(val key: Int, val value: Long){
    HOUR(0,3600000L),
    DAY(1,86400000L),
    MONTH(2,604800000L),
    YEAR(3,2592000000L)
}





