package com.cvelez.photos.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.cvelez.photos.core.BaseViewHolder
import com.cvelez.photos.data.model.AlbumItem
import com.bumptech.glide.Glide
import com.cvelez.photos.R
import com.cvelez.photos.databinding.PhotographRowBinding


class MainAdapter(
    private val context: Context,
    private val itemClickListener: OnPhotoClickListener,
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var photosList = listOf<AlbumItem>()

    interface OnPhotoClickListener {
        fun onPhotographClick(photo: AlbumItem, position: Int)
    }

    fun setPhotographList(drinksList: List<AlbumItem>) {
        this.photosList = drinksList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = PhotographRowBinding.inflate(LayoutInflater.from(context), parent, false)

        val holder = MainViewHolder(itemBinding)

        itemBinding.root.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
            itemClickListener.onPhotographClick(photosList[position], position)
        }
        return holder
    }

    override fun getItemCount(): Int = photosList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(photosList[position])
        }
    }

    private inner class MainViewHolder(
        val binding: PhotographRowBinding
    ) :
        BaseViewHolder<AlbumItem>(binding.root) {
        override fun bind(item: AlbumItem) = with(binding) {
            Glide.with(context)
                .load(item.url)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.icono_logo)
                .error(R.drawable.ic_baseline_error_outline)
                .into(imgPhotograph)

            textName.text = item.title
            textDescription.text = item.id.toString()
        }
    }
}