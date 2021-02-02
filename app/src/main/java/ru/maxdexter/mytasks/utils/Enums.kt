package ru.maxdexter.mytasks.utils


enum class TimeRange(val value: Long){
    MINUTE(60000L),
    HOUR(3600000L),
    DAY(86400000L),
    MONTH(604800000L),
    YEAR(2592000000L)
}


