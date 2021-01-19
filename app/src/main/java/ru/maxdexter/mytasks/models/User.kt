package ru.maxdexter.mytasks.models

import android.net.Uri
import io.realm.RealmObject

open class User : RealmObject(){
    var phone: String = "0"
}
