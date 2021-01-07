package ru.maxdexter.mytasks.ui.newtask

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.FragmentNewTaskBinding
import java.text.DateFormat
import java.util.*

class NewTaskFragment : BottomSheetDialogFragment() {

    private lateinit var  binding: FragmentNewTaskBinding

    @SuppressLint("ShowToast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        // Inflate the layout for this fragment

        binding.tvDateChange.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.show()
        }

        val listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            Log.i("TIME_PICKER", "$hourOfDay $minute")
        }

        binding.tvTimeChange.setOnClickListener {
            TimePickerDialog(requireContext(),android.R.style.Theme_Material_Dialog,listener,12,50,true).show()
        }




        return binding.root
    }


}