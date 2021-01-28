package ru.maxdexter.mytasks.ui.newtask

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.adapters.FileAdapter
import ru.maxdexter.mytasks.databinding.FragmentNewTaskBinding
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.repository.Repository
import ru.maxdexter.mytasks.repository.localdatabase.RoomDb
import ru.maxdexter.mytasks.utils.textListener
import java.util.*

class NewTaskFragment : BottomSheetDialogFragment() {

    private val REQUEST_CODE: Int = 123
    private lateinit var  binding: FragmentNewTaskBinding
    private val calendar = Calendar.getInstance(Locale.getDefault())
    private val date = Calendar.Builder()
    private val viewModel by lazy {
        val db: RoomDb = RoomDb.invoke(requireContext())
        val repository: LocalDatabase = Repository(db.getDao())
        ViewModelProvider(this,NewTaskViewModelFactory(repository)).get(NewTaskViewModel::class.java)
    }
    private val adapter: FileAdapter by lazy {
        FileAdapter()
    }
    @SuppressLint("ShowToast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        dateObserver()
        timeObserver()
        initDatePicker()
        initTimePicker()

        binding.btnAdd.setOnClickListener {
            val title = binding.tvTitle.text.toString()
            val desc = binding.tvTaskDescription.text.toString()

            viewModel.saveTask(title, desc)
            dismiss()
        }
        binding.ivAddFile.setOnClickListener {
            getFile()
        }

        initRecycler()
        return binding.root
    }

    private fun initRecycler() {
        viewModel.fileList.observe(viewLifecycleOwner, {
            binding.rvFile.layoutManager = LinearLayoutManager(context)
            adapter.submitList(it)
            binding.rvFile.adapter = adapter
        })
    }

    private fun dateObserver() {
        viewModel.liveDate.observe(viewLifecycleOwner, {
            binding.tvDateChange.text = it
        })
    }

    private fun timeObserver() {
        viewModel.liveTime.observe(viewLifecycleOwner, {
            binding.tvTimeChange.text = it
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            viewModel.setDate(year,month,dayOfMonth)
        }
        binding.tvDateChange.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(), listener, year, month, day)
            datePickerDialog.show()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initTimePicker() {
        val listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.setTime(hourOfDay,minute)
          //  val timestamp = Timestamp(date.time)
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        binding.tvTimeChange.setOnClickListener {
            TimePickerDialog(requireContext(), listener, hour, minute, true).show()
        }
    }
    private fun getFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.saveFile(requestCode,resultCode,data)
    }


}