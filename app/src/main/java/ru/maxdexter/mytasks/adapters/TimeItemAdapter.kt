package ru.maxdexter.mytasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.ItemTaskLayoutBinding
import ru.maxdexter.mytasks.models.Task

class TimeItemAdapter : ListAdapter<Task, TimeItemAdapter.TaskViewHolder>(DiffCallback()){



    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            TODO("Not yet implemented")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class TaskViewHolder(private val binding:ItemTaskLayoutBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Task){

        }

        companion object{
            fun from(parent: ViewGroup): ItemTaskLayoutBinding{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTaskLayoutBinding.inflate(layoutInflater,parent)
                return binding
            }
        }
    }


}


