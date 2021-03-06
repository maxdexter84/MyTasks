package ru.maxdexter.mytasks.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.maxdexter.mytasks.databinding.ItemHourLayoutBinding
import ru.maxdexter.mytasks.domen.models.Hour
import ru.maxdexter.mytasks.ui.calendar.CalendarViewModel

class HourAdapter(private val viewModel: CalendarViewModel): RecyclerView.Adapter<HourAdapter.ViewHolder>() {


     var list = mutableListOf<Hour>()

    class ViewHolder(val binding: ItemHourLayoutBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Hour, viewModel: CalendarViewModel){
            binding.tvPeriod.text = "${item.startPeriod}:00"
            if (item.list != null){
                binding.recyclerView.visibility = RecyclerView.VISIBLE
                binding.recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                val adapter = TimeItemAdapter(object : TimeItemAdapter.ItemListener{
                    override fun click(uuid: String) {
                        viewModel.selectedTask(uuid)
                    }
                })
                adapter.submitList(item.list)
                binding.recyclerView.adapter = adapter
            }
        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHourLayoutBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding, parent.context)
            }        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, viewModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}