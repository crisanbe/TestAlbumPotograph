package com.cvelez.photos.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvelez.photos.R
import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.databinding.FragmentMainPhotographsBinding
import com.cvelez.photos.presentacion.MainViewModel
import com.cvelez.photos.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPhotographs : Fragment(R.layout.fragment_main_photographs),
    MainAdapter.OnDrinkClickListener {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mainAdapter = MainAdapter(requireContext(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainPhotographsBinding.bind(view)

        binding.rvPhotograph.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPhotograph.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.btnFavoritos.setOnClickListener {
            findNavController().navigate(R.id.action_mainPhotographs_to_favoritesFragment)
        }

        binding.rvPhotograph.adapter = mainAdapter

        binding.search.onQueryTextChanged {
            viewModel.setPhotograph(it)
        }

        viewModel.fetchPhotographsList.observe(viewLifecycleOwner, Observer { result ->
            binding.progressBar.showIf { result is Resource.Loading }

            when (result) {
                is Resource.Loading -> {
                    binding.emptyContainer.root.hide()
                }
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        binding.rvPhotograph.hide()
                        binding.emptyContainer.root.show()
                        return@Observer
                    }
                    binding.rvPhotograph.show()
                    mainAdapter.setPhotographList(result.data)
                    binding.emptyContainer.root.hide()
                }
                is Resource.Failure -> {
                    Log.i("testerror","${result.exception}")
                    showToast("Ocurrió un error al traer los datos ${result.exception.message}")
                }
                else -> Unit
            }
        })
    }

    override fun onPhotographClick(photo: AlbumItem, position: Int) {
        findNavController().navigate(
            MainPhotographsDirections.actionMainPhotographsToDetails(
                photo
            )
        )
    }
}