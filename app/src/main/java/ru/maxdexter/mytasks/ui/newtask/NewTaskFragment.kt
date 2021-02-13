package ru.maxdexter.mytasks.ui.newtask

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentResolverCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.adapters.FileAdapter
import ru.maxdexter.mytasks.databinding.FragmentNewTaskBinding
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.utils.Alarm
import ru.maxdexter.mytasks.utils.CheckNetwork
import ru.maxdexter.mytasks.utils.REQUEST_CODE
import java.util.*

class NewTaskFragment : BottomSheetDialogFragment() {
    private val REQUEST_CODE_IMAGE = 100
    private val REQUEST_CODE_PERMISSIONS = 101

    private val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
    private val MAX_NUMBER_REQUEST_PERMISSIONS = 2
    private var permissionRequestCount: Int = 0
    private val permissions = Arrays.asList(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    private lateinit var  binding: FragmentNewTaskBinding
    private val calendar = Calendar.getInstance(Locale.getDefault())
    private val alarmManager by lazy {
        requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    private val currentTaskUUID by lazy {
        arguments?.let { NewTaskFragmentArgs.fromBundle(it) }?.taskUUID
    }

    private val newTaskViewModel: NewTaskViewModel by viewModel { parametersOf(currentTaskUUID) }


    private val adapter: FileAdapter by lazy {
        FileAdapter()
    }
    @SuppressLint("ShowToast")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        requestPermissionsIfNecessary()
        newTaskViewModel.titleAndDescription.observe(viewLifecycleOwner,{pair->
            pair?.let {
                binding.tvTitle.setText(it.first)
                binding.tvTaskDescription.setText(it.second)
            }

        })
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
            val alarm = binding.swAlarm.isChecked
            val repeat = binding.switchRepeatTask.isChecked
            val repeatRange = if(binding.switchRepeatTask.isChecked)binding.spinnerUnit.selectedItemPosition else -1
            newTaskViewModel.saveTaskChange(title, desc,alarm, repeat, repeatRange)
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
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.setType("*/*")
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true)
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


    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            if (permissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                permissionRequestCount += 1
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions.toTypedArray(),
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.set_permissions_in_settings,
                    Toast.LENGTH_LONG
                ).show()
                binding.ivAddFile.isEnabled = false
            }
        }
    }

    /** Permission Checking  */
    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in permissions) {
            hasPermissions = hasPermissions and isPermissionGranted(permission)
        }
        return hasPermissions
    }

    private fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(requireContext(), permission) ==
                PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            requestPermissionsIfNecessary() // no-op if permissions are granted already.
        }
    }


}