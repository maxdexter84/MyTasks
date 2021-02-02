package ru.maxdexter.mytasks.ui.calendar

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.adapters.HourAdapter
import ru.maxdexter.mytasks.adapters.TimeItemAdapter
import ru.maxdexter.mytasks.alarm.NotificationReceiver
import ru.maxdexter.mytasks.databinding.FragmentCalendarBinding
import ru.maxdexter.mytasks.models.Hour
import ru.maxdexter.mytasks.models.TaskWithTaskFile
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.repository.Repository
import ru.maxdexter.mytasks.repository.firebase.Auth
import ru.maxdexter.mytasks.repository.localdatabase.RoomDb
import java.time.Month
import java.time.MonthDay
import java.time.Year
import java.time.YearMonth
import java.util.*

class CalendarFragment : Fragment() {

    private val calendarViewModel: CalendarViewModel by lazy {
        val db = RoomDb.invoke(requireContext())
        val repository: LocalDatabase = Repository(db.getDao())
        ViewModelProvider(this,CalendarViewModelFactory(repository)).get(CalendarViewModel::class.java)
    }
    private lateinit var binding: FragmentCalendarBinding
    private val hourAdapter: HourAdapter by lazy {
        HourAdapter(calendarViewModel)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calendar,container, false)
        calendarViewModel.loadData(Year.now().value,YearMonth.now().month.value,MonthDay.now().dayOfMonth)
        calendarListener()

        calendarViewModel.listTaskFile.observe(viewLifecycleOwner,{
            val hourList = calendarViewModel.updateData(it)
            initRecycler(hourList)
        })


        initBottomAppBar()

        calendarViewModel.selectedTask.observe(viewLifecycleOwner,{
            if (it != ""){
                findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToDetailFragment(it))
            }

        })
        binding.fab.setOnClickListener { findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToNewTaskFragment()) }
        val calendar =   Calendar.getInstance(Locale.getDefault())

        alarmStart(requireContext(),"cbsjdbc","ldsnackjd",calendar.timeInMillis)
        return binding.root
    }

    private fun calendarListener() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendarViewModel.loadData(year, month, dayOfMonth)
        }
    }


    private fun initRecycler(hourList: MutableList<Hour>) {
        binding.recyclerTable.apply {
            hourAdapter.list = hourList
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hourAdapter
        }
    }

    private fun initBottomAppBar() {
        binding.bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.navigation_notifications -> {
                    findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToNotificationsFragment())
                    true
                }
                R.id.navigation_profile -> {
                    findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun alarmStart(context: Context, title: String, text: String, time: Long){
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("title",title)
        intent.putExtra("text",text)
        val pendingIntent = PendingIntent.getBroadcast(context,84,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pendingIntent)

    }

}