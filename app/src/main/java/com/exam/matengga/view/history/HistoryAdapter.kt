package com.exam.matengga.view.history

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exam.matengga.R
import com.exam.matengga.data.local.entity.HistoryEntity
import com.exam.matengga.databinding.ItemHistoryBinding
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private val onDeleteClick: (HistoryEntity) -> Unit) :
    ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: HistoryEntity) {
            binding.fruitName.text = history.fruitName
            binding.ripeness.text = history.ripeness
            binding.fruitName.text = history.fruitName.let { translateFruitName(it) }
            binding.ripeness.text = history.ripeness.let { translateRipeness(it) }
            binding.timestamp.text = DateFormat.getDateTimeInstance().format(Date(history.timestamp))

            try {
                Glide.with(binding.root.context)
                    .load(Uri.parse(history.imageUri))
                    .placeholder(R.drawable.ic_pc_image)
                    .error(R.drawable.ic_broken_image)
                    .into(binding.fruitImage)
            } catch (e: SecurityException) {
                Log.e("HistoryAdapter", "Failed to load image: ${history.imageUri}", e)
                binding.fruitImage.setImageResource(R.drawable.ic_broken_image)
            }

            binding.root.setOnLongClickListener {
                onDeleteClick(history)
                true
            }
        }

        private fun translateRipeness(ripeness: String): String {
            return when (ripeness.lowercase(Locale.ROOT)) {
                "unripe" -> "Belum matang"
                "ripe" -> "Matang"
                else -> "Tidak diketahui"
            }
        }

        private fun translateFruitName(fruitName: String): String {
            return when (fruitName.lowercase(Locale.ROOT)) {
                "durian" -> "Durian"
                "strawberry" -> "Stroberi"
                "grape" -> "Anggur"
                "dragon fruit" -> "Buah Naga"
                else -> fruitName
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HistoryEntity>() {
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}
