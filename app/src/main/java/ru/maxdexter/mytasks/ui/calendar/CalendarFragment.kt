package ru.maxdexter.mytasks.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.maxdexter.mytasks.MobileNavigationDirections
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.adapters.HourAdapter
import ru.maxdexter.mytasks.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var binding: FragmentCalendarBinding
    private val hourAdapter: HourAdapter by lazy {
        HourAdapter()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calendar,container, false)
        calendarViewModel =
                ViewModelProvider(this).get(CalendarViewModel::class.java)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Toast.makeText(requireContext(),"$dayOfMonth $month $year ${view.date}",Toast.LENGTH_SHORT).show()

        }


        binding.recyclerTable.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hourAdapter
        }



        binding.bottomAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.navigation_note ->{
                    true
                }
                R.id.navigation_notifications -> {
                    findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToNotificationsFragment())
                    true
                }
                R.id.navigation_dashboard -> {
                    findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToProfileFragment())
                    true
                }
                else -> false
            }


        }

        binding.fab.setOnClickListener { findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToNewTaskFragment()) }
        return binding.root
    }
}