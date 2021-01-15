package ru.maxdexter.mytasks.models

import android.net.Uri

data class User(val name: String? = null,
                val email: String? = null,
                val phone: String? = null,
                val photo: Uri? = null,
                val isAnonymous: Boolean? )
