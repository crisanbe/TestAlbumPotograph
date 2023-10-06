package com.cvelez.photos.ui.photodetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.cvelez.photos.R
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.databinding.FragmentDetailsBinding
import com.cvelez.photos.presentacion.MainViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Details : Fragment(R.layout.fragment_details) {

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var photo: AlbumItem
    private var isPhotoFavorited: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let {
            DetailsArgs.fromBundle(it).also { args ->
                photo = args.photo
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        Glide.with(requireContext())
            .load(photo.url)
            .centerCrop()
            .into(binding.imgDrinkDetails)

        binding.titlePhoto.text = photo.title
        binding.titleAlbum.text = photo.albumId.toString()
        binding.descriptionPhoto.text = photo.id.toString()

        val photos: ArrayList<String> = arrayListOf()

        var result: String? = ""

        photos.forEach { value ->
            result += "* $value\n\n"
        }

        binding.photos.text = result

        fun updateButtonIcon() {
            val isDrinkFavorited = isPhotoFavorited ?: return

            binding.btnSaveOrDeletePhotograph.setImageResource(
                when {
                    isDrinkFavorited -> R.drawable.ic_baseline_delete
                    else -> R.drawable.ic_baseline_save
                }
            )
        }

        binding.btnSaveOrDeletePhotograph.setOnClickListener {
            val isDrinkFavorited = isPhotoFavorited ?: return@setOnClickListener

            viewModel.saveOrDeleteFavoritePhotograph(photo)
            this.isPhotoFavorited = !isDrinkFavorited
            updateButtonIcon()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            isPhotoFavorited = viewModel.isPhotographFavorite(photo)
            updateButtonIcon()
        }
    }
}