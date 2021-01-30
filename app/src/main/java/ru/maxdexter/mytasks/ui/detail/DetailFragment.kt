package ru.maxdexter.mytasks.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.InternalCoroutinesApi
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.DetailFragmentBinding
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.repository.Repository
import ru.maxdexter.mytasks.repository.localdatabase.RoomDb
class DetailFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }
    lateinit var binding: DetailFragmentBinding

    private val currentTaskUUID by lazy {
        arguments?.let { DetailFragmentArgs.fromBundle(it) }?.uuid
    }

    private val viewModel by lazy {
        val db: RoomDb = RoomDb.invoke(requireContext())
        val repository: LocalDatabase = Repository(db.getDao())
        ViewModelProvider(this, DetailViewModelFactory(repository,currentTaskUUID ?: "")).get(DetailViewModel::class.java)
    }




    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.detail_fragment, container, false)


        saveBtnInit()
        viewModel.currentTaskWithTaskFile.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.tvTitle.setText(it.task?.title)
                binding.tvTaskDescription.setText(it.task?.description)
                binding.tvDateChange.text = "${it.task?.eventDay}.${it.task?.eventMonth }.${it.task?.eventYear}"
                binding.tvTimeChange.text = "${it.task?.eventHour} : ${it.task?.eventMinute}"
            }
        })

        return binding.root
    }

    private fun saveBtnInit() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }


}