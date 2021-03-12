package ru.maxdexter.mytasks.ui.fragments.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.ui.adapters.HourAdapter
import ru.maxdexter.mytasks.databinding.FragmentCalendarBinding
import ru.maxdexter.mytasks.ui.entity.Hour


class CalendarFragment : Fragment() {

    private val calendarViewModel: CalendarViewModel by viewModel()
    private lateinit var binding: FragmentCalendarBinding
    private val hourAdapter: HourAdapter by lazy {
        HourAdapter(calendarViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.let { CalendarFragmentArgs.fromBundle(it) }?.taskUUID
        if (args != null && args != "empty"){
            findNavController().navigate(
                CalendarFragmentDirections.actionCalendarFragmentToNewTaskFragment(
                    args
                )
            )
        }
    }





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calendar,container, false)
        calendarListener()
        initBottomAppBar()
        selectedTaskObserver()
        initFab()

        return binding.root
    }


    private fun initFab() {
        binding.fab.setOnClickListener {
            findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToNewTaskFragment())
        }
    }



    private fun selectedTaskObserver() {
        calendarViewModel.selectedTask.observe(viewLifecycleOwner,  {
            if (it != "") {
                findNavController().navigate(
                    CalendarFragmentDirections.actionCalendarFragmentToNewTaskFragment(
                        it
                    )
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        calendarViewModel.listUITask.observe(viewLifecycleOwner,  {
            val hourList = calendarViewModel.updateData(it)
            initRecycler(hourList)
        })


    }



    private fun calendarListener() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
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



}