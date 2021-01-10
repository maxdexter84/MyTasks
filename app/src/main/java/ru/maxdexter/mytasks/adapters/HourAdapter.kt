package ru.maxdexter.mytasks.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.maxdexter.mytasks.databinding.ItemHourLayoutBinding
import ru.maxdexter.mytasks.models.Hour

class HourAdapter: RecyclerView.Adapter<HourAdapter.ViewHolder>() {


    private val list = mutableListOf<Hour>()
    init {
        for (i in 0..23 step 1){
            list.add(Hour(i,("$i" + "59").toInt()))
        }
    }
    class ViewHolder(val binding: ItemHourLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Hour){
            binding.tvPeriod.text = "${item.startPeriod}:00"
        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHourLayoutBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}