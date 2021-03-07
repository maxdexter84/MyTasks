package ru.maxdexter.mytasks.ui.entity



data class UIFile ( val uri: String = "",
                    val saveToCloud: Boolean = false,
                    val taskUUID: String = "",
                    val fileType: String = "",
                    val name: String = "")