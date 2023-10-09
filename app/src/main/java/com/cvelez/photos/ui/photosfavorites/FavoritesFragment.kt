package com.cvelez.photos.ui.photosfavorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvelez.photos.R
import com.cvelez.photos.databinding.FragmentFavoritesBinding
import com.cvelez.photos.ui.viewmodel.MainViewModel
import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.utils.show
import com.cvelez.photos.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites),
    FavoritesAdapter.OnDrinkClickListener {

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesAdapter = FavoritesAdapter(requireContext(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFavoritesBinding.bind(view)

        binding.rvDrinksFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDrinksFavorites.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvDrinksFavorites.adapter = favoritesAdapter

        viewModel.getFavoritePhotographs().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        binding.emptyContainer.root.show()
                        return@observe
                    }
                    favoritesAdapter.setDrinkList(result.data)
                }
                is Resource.Failure -> {
                    showToast("An error occurred ${result.exception}")
                }
            }
        }
    }


    override fun onDrinkClick(drink: AlbumItem, position: Int) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToDetails(
                drink
            )
        )
    }

    override fun onDrinkLongClick(drink: AlbumItem, position: Int) {
        viewModel.deleteFavoritePhotograph(drink)
    }

}