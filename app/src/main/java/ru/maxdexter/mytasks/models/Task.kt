package ru.maxdexter.mytasks.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Task : RealmObject() {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var title: String? = null
    var description: String? = null
    var eventDate: Long? = null
    var eventTime: Int? = null
    var isCompleted: Boolean = false
    var repeat: Boolean = false
    var file: RealmList<TaskFile>? = null
    var user: User? = null
}