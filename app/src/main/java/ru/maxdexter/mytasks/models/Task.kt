package ru.maxdexter.mytasks.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

data class Task(@PrimaryKey
                val id: String = UUID.randomUUID().toString(),
                val title: String,
                val description: String,
                val eventDate: Long,
                val eventTime: Int,
                val isCompleted: Boolean = false,
                val repeat: Boolean = false,
                val file: List<String>,
                val user: User
                ) : RealmObject()