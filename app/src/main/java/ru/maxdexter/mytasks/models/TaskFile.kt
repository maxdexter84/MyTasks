package ru.maxdexter.mytasks.models

import android.net.Uri
import io.realm.RealmObject

open class TaskFile : RealmObject(){
    var uri: String = ""
}
