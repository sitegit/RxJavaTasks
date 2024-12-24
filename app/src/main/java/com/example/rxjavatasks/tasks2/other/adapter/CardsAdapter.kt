package com.example.rxjavatasks.tasks2.other.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavatasks.databinding.CardsItemBinding
import com.example.rxjavatasks.tasks2.other.network.DiscountCard

class CardsAdapter : ListAdapter<DiscountCard, CardsAdapter.CardsViewHolder>(
    CardsDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        return CardsViewHolder(
            CardsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class CardsViewHolder(
        private val binding: CardsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DiscountCard) {
            binding.apply {
                cardNameTextView.text = item.name
                cardDiscountTextView.text = item.discount
            }
        }
    }
}

class CardsDiffCallback : DiffUtil.ItemCallback<DiscountCard>() {
    override fun areItemsTheSame(oldItem: DiscountCard, newItem: DiscountCard): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: DiscountCard, newItem: DiscountCard): Boolean {
        return oldItem == newItem
    }
}

