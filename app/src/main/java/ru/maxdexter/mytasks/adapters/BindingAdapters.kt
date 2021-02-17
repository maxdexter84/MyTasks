package ru.maxdexter.mytasks.adapters

import android.content.Context
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import ru.maxdexter.mytasks.ui.newtask.NewTaskViewModel
import java.lang.StringBuilder

//
//@BindingAdapter
//fun TextInputEditText.titleListener(viewModel: NewTaskViewModel) {
//    val stringBuilder = StringBuilder()
//    this.addTextChangedListener {
//        stringBuilder.clear()
//        stringBuilder.append(it.toString())
//        viewModel.saveTitleChange(stringBuilder.toString())
//    }
//
//}