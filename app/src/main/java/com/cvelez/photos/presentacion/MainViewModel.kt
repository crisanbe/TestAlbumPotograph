@file:Suppress("DEPRECATION")

package com.cvelez.photos.presentacion

import androidx.hilt.Assisted
import androidx.lifecycle.*
import com.cvelez.photos.application.ToastHelper
import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.domain.PhotographRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PhotographRepository,
    private val toastHelper: ToastHelper,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentPhotographId = savedStateHandle.getLiveData("1", "")

    fun setPhotograph(photoId: String) {
        currentPhotographId.value = photoId
    }

    val fetchPhotographsList = currentPhotographId.distinctUntilChanged().switchMap { nameDrink ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                repository.getPhotographById(nameDrink).collect {
                    emit(it)
                }
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
    }

    fun saveOrDeleteFavoritePhotograph(photo: AlbumItem) {
        viewModelScope.launch {
            if (repository.isPhotographFavorite(photo)) {
                repository.deleteFavoritePhotograph(photo)
                toastHelper.sendToast("photo removed from favorites")
            } else {
                repository.saveFavoritePhotograph(photo)
                toastHelper.sendToast("Photografia saved in favorites")
            }
        }
    }

    fun getFavoritePhotographs() =
        liveData<Resource<List<AlbumItem>>>(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emitSource(repository.getFavoritesPhotographs().map { Resource.Success(it) })
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun deleteFavoritePhotograph(photo: AlbumItem) {
        viewModelScope.launch {
            repository.deleteFavoritePhotograph(photo)
            toastHelper.sendToast("photo deleted from favorites")
        }
    }

    suspend fun isPhotographFavorite(photo: AlbumItem): Boolean =
        repository.isPhotographFavorite(photo)
}