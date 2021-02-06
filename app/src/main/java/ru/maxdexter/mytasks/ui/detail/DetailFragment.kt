package ru.maxdexter.mytasks.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.viewmodel.compat.ViewModelCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.DetailFragmentBinding

class DetailFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }
    lateinit var binding: DetailFragmentBinding

    private val currentTaskUUID by lazy {
        arguments?.let { DetailFragmentArgs.fromBundle(it) }?.uuid
    }

    private val detailViewModel: DetailViewModel by viewModel { parametersOf(currentTaskUUID)}




    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.detail_fragment, container, false)


        saveBtnInit()
        detailViewModel.currentTaskWithTaskFile.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.tvTitle.setText(it.task?.title)
                binding.tvTaskDescription.setText(it.task?.description)
            }
        })
        detailViewModel.taskDate.observe(viewLifecycleOwner,{
            binding.tvDateChange.text = it
        })

        detailViewModel.taskTime.observe(viewLifecycleOwner,{
            binding.tvTimeChange.text = it
        })

        return binding.root
    }

    private fun saveBtnInit() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }


}