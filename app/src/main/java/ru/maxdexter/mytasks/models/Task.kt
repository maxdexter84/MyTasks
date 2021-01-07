package ru.maxdexter.mytasks.models

data class Task(val id: Int ,
                val title: String,
                val description: String,
                val eventDate: Long,
                val eventTime: Int,
                val isCompleted: Boolean = false,
                val repeat: Boolean = false, val file: String, val image: String)