package ru.maxdexter.mytasks.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxdexter.mytasks.databinding.ItemTaskLayoutBinding
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.TaskWithTaskFile

class TimeItemAdapter : ListAdapter<TaskWithTaskFile, TimeItemAdapter.TaskViewHolder>(DiffCallback()){



    class DiffCallback : DiffUtil.ItemCallback<TaskWithTaskFile>() {
        override fun areItemsTheSame(
            oldItem: TaskWithTaskFile,
            newItem: TaskWithTaskFile
        ): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TaskWithTaskFile,
            newItem: TaskWithTaskFile
        ): Boolean {
           return oldItem.task == newItem.task
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder(private val binding:ItemTaskLayoutBinding ) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item: TaskWithTaskFile){
            binding.tvTitle.text = item.task?.title
            binding.tvDescription.text = item.task?.description
            binding.tvTaskTime.text = "${item.task?.eventHour} : ${item.task?.eventMinute}"
        }

        companion object{
            fun from(parent: ViewGroup): TaskViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                return TaskViewHolder(ItemTaskLayoutBinding.inflate(layoutInflater,parent,false))
            }
        }
    }


}


