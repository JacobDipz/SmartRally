package com.example.badmintonai.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.badmintonai.databinding.LogLayoutBinding
import com.example.badmintonai.fragments.HomeFragmentDirections
import com.example.badmintonai.model.Log

class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(val itemBinding: LogLayoutBinding): RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Log>(){
        override fun areItemsTheSame(oldItem: Log, newItem: Log): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.logDesc == newItem.logDesc &&
                    oldItem.logTitle == newItem.logTitle &&
                    oldItem.videoPath == newItem.videoPath
        }

        override fun areContentsTheSame(oldItem: Log, newItem: Log): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(
            LogLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val currentLog = differ.currentList[position]

        holder.itemBinding.LogTitle.text = currentLog.logTitle
        holder.itemBinding.LogDesc.text = currentLog.logDesc

        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToEditLogFragment(currentLog)
            it.findNavController().navigate(direction)
        }
    }
}