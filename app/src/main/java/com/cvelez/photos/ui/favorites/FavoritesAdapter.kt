package com.cvelez.photos.ui.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.cvelez.photos.core.BaseViewHolder
import com.cvelez.photos.data.model.AlbumItem
import com.bumptech.glide.Glide
import com.cvelez.photos.databinding.PhotographRowBinding

class FavoritesAdapter(
    private val context: Context,
    private val itemClickLister: OnDrinkClickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var drinkList = listOf<AlbumItem>()

    interface OnDrinkClickListener {
        fun onDrinkClick(drink: AlbumItem, position: Int)
        fun onDrinkLongClick(drink: AlbumItem, position: Int)
    }

    fun setDrinkList(drinkList: List<AlbumItem>) {
        this.drinkList = drinkList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = PhotographRowBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = MainViewHolder(itemBinding)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION }
                ?: return@setOnClickListener

            itemClickLister.onDrinkClick(drinkList[position], position)
        }

        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION }
                ?: return@setOnLongClickListener true

            itemClickLister.onDrinkLongClick(drinkList[position], position)
            return@setOnLongClickListener true
        }
        return holder
    }

    override fun getItemCount(): Int = drinkList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(drinkList[position])
        }
    }

    private inner class MainViewHolder(
        private val binding: PhotographRowBinding
    ) :
        BaseViewHolder<AlbumItem>(binding.root) {
        override fun bind(item: AlbumItem) = with(binding) {
            Glide.with(context)
                .load(item.thumbnailUrl)
                .centerCrop()
                .into(imgPhotograph)

            textName.text = item.title
            textDescription.text = item.id.toString()

        }
    }
}