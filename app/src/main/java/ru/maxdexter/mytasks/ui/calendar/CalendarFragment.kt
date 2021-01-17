package ru.maxdexter.mytasks.ui.calendar

import android.app.Activity
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
import ru.maxdexter.mytasks.databinding.FragmentCalendarBinding
import ru.maxdexter.mytasks.repository.firebase.Auth
import ru.maxdexter.mytasks.utils.Constants

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

        initBottomAppBar()

        binding.fab.setOnClickListener { findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToNewTaskFragment()) }
        return binding.root
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





}