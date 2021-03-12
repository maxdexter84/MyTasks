package ru.maxdexter.mytasks.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxdexter.mytasks.databinding.ItemTaskLayoutBinding
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskWithTaskFile
import ru.maxdexter.mytasks.ui.entity.UITask
import ru.maxdexter.mytasks.utils.handleParseTime

class TimeItemAdapter(private val itemListener: ItemListener) : ListAdapter<UITask, TimeItemAdapter.TaskViewHolder>(DiffCallback()){


    interface ItemListener{
        fun click(uuid: String)
    }
    class DiffCallback : DiffUtil.ItemCallback<UITask>() {
        override fun areItemsTheSame(
            oldItem: UITask,
            newItem: UITask
        ): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: UITask,
            newItem: UITask
        ): Boolean {
           return oldItem.id == newItem.id
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
        fun bind(item: UITask, listener: ItemListener){
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
            binding.tvTaskTime.text = handleParseTime(item)
            itemView.setOnClickListener {
                item.let { it1 -> listener.click(it1.id) }
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


