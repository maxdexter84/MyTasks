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
import com.google.firebase.Timestamp
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.FragmentNewTaskBinding
import java.sql.Time
import java.text.DateFormat
import java.time.Month
import java.time.Year
import java.util.*

class NewTaskFragment : BottomSheetDialogFragment() {

    private lateinit var  binding: FragmentNewTaskBinding
    private val calendar = Calendar.getInstance(Locale.getDefault())
    @SuppressLint("ShowToast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        // Inflate the layout for this fragment
        initDatePicker()

        initTimePicker()




        return binding.root
    }

    private fun initDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            Log.i("DATE_PICKER","$year $month $dayOfMonth")
        }
        binding.tvDateChange.setOnClickListener {

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(), listener, year, month, day)
            datePickerDialog.show()
        }
    }


    private fun initTimePicker() {
        val listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            Log.i("TIME_PICKER","$hourOfDay $minute")
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        binding.tvTimeChange.setOnClickListener {
            TimePickerDialog(requireContext(), listener, hour, minute, true).show()
        }
    }


}