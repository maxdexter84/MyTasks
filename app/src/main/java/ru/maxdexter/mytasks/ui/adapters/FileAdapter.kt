package ru.maxdexter.mytasks.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxdexter.mytasks.databinding.ItemFileBinding
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import ru.maxdexter.mytasks.ui.entity.UIFile
import ru.maxdexter.mytasks.utils.extension.setImagePrev

class FileAdapter: ListAdapter<UIFile,FileAdapter.ViewHolder>(TaskDiffCallback()) {
    class ViewHolder(val binding: ItemFileBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UIFile){
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

    class TaskDiffCallback :  DiffUtil.ItemCallback<UIFile>(){
        override fun areItemsTheSame(oldItem: UIFile, newItem: UIFile): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UIFile, newItem: UIFile): Boolean {
           return oldItem.uri == newItem.uri
        }

    }
}
