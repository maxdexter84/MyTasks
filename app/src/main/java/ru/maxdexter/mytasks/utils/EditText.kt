package ru.maxdexter.mytasks.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.databinding.adapters.TextViewBindingAdapter
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.textListener(): String{
    var res: String = ""

    val listener = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            res = s.toString()
        }
    }
    this.addTextChangedListener(listener)
    return res
}