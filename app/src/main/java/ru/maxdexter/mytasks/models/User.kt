package ru.maxdexter.mytasks.models

import android.net.Uri
import io.realm.RealmObject

data class User(val phone: String = "0") : RealmObject()
