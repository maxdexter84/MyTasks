package ru.maxdexter.mytasks.ui.newtask

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.adapters.FileAdapter
import ru.maxdexter.mytasks.databinding.FragmentNewTaskBinding
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.utils.Alarm
import ru.maxdexter.mytasks.utils.CheckNetwork
import ru.maxdexter.mytasks.utils.REQUEST_CODE
import java.util.*

class NewTaskFragment : BottomSheetDialogFragment() {

    private lateinit var  binding: FragmentNewTaskBinding
    private val calendar = Calendar.getInstance(Locale.getDefault())
    private val alarmManager by lazy {
        requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    private val currentTaskUUID by lazy {
        arguments?.let { NewTaskFragmentArgs.fromBundle(it) }?.taskUUID
    }

    private val newTaskViewModel: NewTaskViewModel by viewModel()


    private val adapter: FileAdapter by lazy {
        FileAdapter()
    }
    @SuppressLint("ShowToast")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        dateObserver()
        timeObserver()
        initDatePicker()
        initTimePicker()
        initBtnAdd()
        initBntAddFile()
        initRecycler()

        newTaskViewModel.setAlarm.observe(viewLifecycleOwner,{
            it?.let { createReminderAlarm(it) }
        })
        initSwAlarm()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        CheckNetwork(requireContext()).observe(viewLifecycleOwner,{
            newTaskViewModel.isOnline = it
        })
    }
    private fun initSpinner() {
        val arrAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.time_range)
        )
        arrAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.spinnerUnit.adapter = arrAdapter

        binding.spinnerUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(requireContext(),position.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun initSwAlarm() {
        binding.swAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    binding.switchRepeatTask.isEnabled = true
                    binding.switchRepeatTask.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked){
                            binding.spinnerUnit.visibility = View.VISIBLE
                            initSpinner()
                        } else {binding.spinnerUnit.visibility = View.INVISIBLE}
                    }

                }
                false -> {
                    binding.switchRepeatTask.isEnabled = false
                    binding.spinnerUnit.visibility = View.INVISIBLE
                }

            }
        }
    }

    private fun initBntAddFile() {
        binding.ivAddFile.setOnClickListener {
            getFile()
        }
    }

    private fun initBtnAdd() {
        binding.btnAdd.setOnClickListener {
            val title = binding.tvTitle.text.toString()
            val desc = binding.tvTaskDescription.text.toString()

            newTaskViewModel.saveTaskChange(title, desc)
            dismiss()
        }
    }

    private fun initRecycler() {
        newTaskViewModel.fileList.observe(viewLifecycleOwner, {
            binding.rvFile.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter.submitList(it)
            binding.rvFile.adapter = adapter
        })
    }

    private fun dateObserver() {
        newTaskViewModel.liveDate.observe(viewLifecycleOwner, {
            binding.tvDateChange.text = it
        })
    }

    private fun timeObserver() {
        newTaskViewModel.liveTime.observe(viewLifecycleOwner, {
            binding.tvTimeChange.text = it
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            newTaskViewModel.setDate(year,month,dayOfMonth)
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
            newTaskViewModel.setTime(hourOfDay,minute)
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
        newTaskViewModel.saveFile(requestCode,resultCode,data)
    }


    private fun createReminderAlarm(task: Task) {
        if (!task.isCompleted) {
            Alarm.createAlarm(
                requireContext(),
                task,
                alarmManager
            )
        }
    }


}