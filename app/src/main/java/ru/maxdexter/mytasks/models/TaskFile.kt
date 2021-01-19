package ru.maxdexter.mytasks.models

import android.net.Uri
import io.realm.RealmObject

data class TaskFile(val uri: Uri) : RealmObject()
