package ru.maxdexter.mytasks.ui.fragments.newtask

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.FragmentNewTaskBinding
import ru.maxdexter.mytasks.utils.alarm.Alarm
import ru.maxdexter.mytasks.ui.adapters.FileAdapter
import ru.maxdexter.mytasks.domen.extension.mapToTaskWithTaskFile
import ru.maxdexter.mytasks.ui.entity.UITask
import ru.maxdexter.mytasks.utils.*
import ru.maxdexter.mytasks.utils.contracts.TakeFileContract
import ru.maxdexter.mytasks.utils.contracts.TakeImageContract
import java.util.*

@InternalCoroutinesApi
class NewTaskFragment : BottomSheetDialogFragment() {

    private lateinit var  binding: FragmentNewTaskBinding
    private val calendar = Calendar.getInstance(Locale.getDefault())
    private val alarmManager by lazy {
        requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val uiTask = UITask()
    private val newTaskViewModel: NewTaskViewModel by viewModel { parametersOf("") }

    private val adapter: FileAdapter by lazy { FileAdapter() }

    private val getPhoto = registerForActivityResult(TakeImageContract()){
           it?.let { newTaskViewModel.createUIFile(it) }
    }
    private val getDocument = registerForActivityResult(TakeFileContract()){ pair->
          pair?.let {  newTaskViewModel.createUIFile(it) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getMultiplePermission().launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE))
    }

    @SuppressLint("ShowToast")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        binding.viewModel = newTaskViewModel
        binding.executePendingBindings()

        dateObserver()
        timeObserver()
        initDatePicker()
        initTimePicker()
        initRecycler()

        newTaskViewModel.setAlarm.observe(viewLifecycleOwner, {
            it?.let { createReminderAlarm(it) }
        })


        initSwAlarm()
        eventObserver()
        return binding.root
    }

    private fun eventObserver(){
        newTaskViewModel.event.observe(viewLifecycleOwner, { event ->
            Toast.makeText(requireContext(),event.name,Toast.LENGTH_SHORT).show()
            when(event){
                NewTaskViewModel.NewTaskEvent.IDL -> Log.i("NEW_TASK_EVENT_LISTENER",event.name)
                NewTaskViewModel.NewTaskEvent.SAVE -> {
                    saveAndClose()
                    dismiss()
                }
                NewTaskViewModel.NewTaskEvent.DELETE -> {
                    newTaskViewModel.deleteTask()
                    dismiss()
                }
                NewTaskViewModel.NewTaskEvent.ADD_PHOTO -> {
                    val uri = newTaskViewModel.createFileImage()
                    getPhoto.launch(uri)
                }
                NewTaskViewModel.NewTaskEvent.ADD_FILE -> getDocument.launch(arrayOf("application/*"))
                NewTaskViewModel.NewTaskEvent.CLOSE -> Log.i("NEW_TASK_EVENT_LISTENER",event.name)
                null->Log.i("NEW_TASK_EVENT_LISTENER","null")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        CheckNetwork(requireContext()).observe(viewLifecycleOwner,Observer{
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
        binding.swAlarm.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    binding.switchRepeatTask.isEnabled = true
                    binding.switchRepeatTask.setOnCheckedChangeListener { _, checked ->
                        if (checked){
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



    @KoinApiExtension
    private fun saveAndClose() {
            val title = binding.tvTitle.text.toString()
            val desc = binding.tvTaskDescription.text.toString()
            val alarm = binding.swAlarm.isChecked
            val repeat = binding.switchRepeatTask.isChecked
            val repeatRange = if(binding.switchRepeatTask.isChecked)binding.spinnerUnit.selectedItemPosition else -1
            val task = uiTask.copy(title = title,description = desc,pushMessage = alarm,repeatRangeValue = repeatRange, repeat = repeat)
            newTaskViewModel.saveTaskChange(title, desc,alarm, repeat, repeatRange)
            dismiss()
    }

    private fun initRecycler() {
        newTaskViewModel.fileList.observe(viewLifecycleOwner, Observer{
            binding.rvFile.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter.submitList(it)
            binding.rvFile.adapter = adapter
        })
    }

    private fun dateObserver() {
        newTaskViewModel.date.observe(viewLifecycleOwner, Observer{
            binding.tvDateChange.text = it
        })
    }

    private fun timeObserver() {
        newTaskViewModel.time.observe(viewLifecycleOwner, Observer{
            binding.tvTimeChange.text = it
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
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
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            newTaskViewModel.setTime(hourOfDay,minute)
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        binding.tvTimeChange.setOnClickListener {
            TimePickerDialog(requireContext(), listener, hour, minute, true).show()
        }
    }

    private fun createReminderAlarm(task: UITask) {
        if (!task.isCompleted) {
            Alarm.createAlarm(
                requireContext(),
                task.mapToTaskWithTaskFile().task,
                alarmManager
            )
        }
    }


    //Узнаем есть ли permissions для камеры
    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }
    //Обрабатываем езультат запроса разрешений
    private fun getMultiplePermission() = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        it.forEach { (t, u) ->
            if (t == Manifest.permission.CAMERA && !u){
                binding.ibAddImage.isEnabled = false
            }else if (t == Manifest.permission.READ_EXTERNAL_STORAGE && !u){
                binding.ibAddFile.isEnabled = false
            }
            Log.i("TAG_PERMISSION", "Permission: $t, granted: $u")
        }
    }

}