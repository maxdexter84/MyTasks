package ru.maxdexter.mytasks.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxdexter.mytasks.databinding.ItemFileBinding
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import ru.maxdexter.mytasks.utils.extension.setImagePrev

class FileAdapter: ListAdapter<TaskFile,FileAdapter.ViewHolder>(TaskDiffCallback()) {
    class ViewHolder(val binding: ItemFileBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskFile){
            binding.ivFile.setImagePrev(item.uri, item.fileType)
            binding.tvFileName.text = item.name

        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFileBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class TaskDiffCallback :  DiffUtil.ItemCallback<TaskFile>(){
        override fun areItemsTheSame(oldItem: TaskFile, newItem: TaskFile): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TaskFile, newItem: TaskFile): Boolean {
           return oldItem.uri == newItem.uri
        }

    }
}
