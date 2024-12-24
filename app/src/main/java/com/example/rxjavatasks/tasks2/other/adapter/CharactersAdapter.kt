package com.example.rxjavatasks.tasks2.other.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavatasks.databinding.CharacterItemBinding
import com.example.rxjavatasks.tasks2.other.network.Character
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class CharactersAdapter : ListAdapter<Character, CharactersAdapter.CharactersViewHolder>(
    CharactersDiffCallback()
) {

    private val _clickSubject = PublishSubject.create<Int>()
    val clickSubject: Observable<Int> = _clickSubject.hide()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(
            CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).also { holder ->
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    _clickSubject.onNext(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class CharactersViewHolder(
        private val binding: CharacterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Character) {
            binding.apply {
                nameTextView.text = item.name
                speciesTextView.text = item.species
            }
        }
    }
}

class CharactersDiffCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}

