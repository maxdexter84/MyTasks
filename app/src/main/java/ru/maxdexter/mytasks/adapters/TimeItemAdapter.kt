package ru.maxdexter.mytasks.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxdexter.mytasks.databinding.ItemTaskLayoutBinding
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.utils.handleParseTime

class TimeItemAdapter(private val itemListener: ItemListener) : ListAdapter<TaskWithTaskFile, TimeItemAdapter.TaskViewHolder>(DiffCallback()){


    interface ItemListener{
        fun click(uuid: String)
    }
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
        holder.bind(getItem(position),itemListener)
    }

    class TaskViewHolder(private val binding:ItemTaskLayoutBinding ) : RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(item: TaskWithTaskFile, listener: ItemListener){
            binding.tvTitle.text = item.task?.title
            binding.tvDescription.text = item.task?.description
            binding.tvTaskTime.text = handleParseTime(item)
            itemView.setOnClickListener {
                item.task?.let { it1 -> listener.click(it1.id) }
            }

        }


        companion object{
            fun from(parent: ViewGroup): TaskViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                return TaskViewHolder(ItemTaskLayoutBinding.inflate(layoutInflater,parent,false))
            }
        }
    }


}


