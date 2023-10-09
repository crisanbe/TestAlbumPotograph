@file:Suppress("DEPRECATION")

package com.cvelez.photos.ui.viewmodel

import androidx.hilt.Assisted
import androidx.lifecycle.*
import com.cvelez.photos.utils.ToastHelper
import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.domain.usecase.GetPhotographsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPhotographsUseCase: GetPhotographsUseCase,
    private val toastHelper: ToastHelper,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentPhotographId = savedStateHandle.getLiveData("1", "")

    fun setPhotograph(photoId: String) {
        currentPhotographId.value = photoId
    }

    val fetchPhotographsList = currentPhotographId.distinctUntilChanged().switchMap { idDrink ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                getPhotographsUseCase.getPhotographById(idDrink).collect {
                    emit(it)
                }
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
    }

    fun saveOrDeleteFavoritePhotograph(photo: AlbumItem) {
        viewModelScope.launch {
            if (getPhotographsUseCase.isPhotographFavorite(photo)) {
                getPhotographsUseCase.deleteFavoritePhotograph(photo)
                toastHelper.sendToast("photo removed from favorites")
            } else {
                getPhotographsUseCase.saveFavoritePhotograph(photo)
                toastHelper.sendToast("Photografia saved in favorites")
            }
        }
    }

    fun getFavoritePhotographs() =
        liveData<Resource<List<AlbumItem>>>(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emitSource(getPhotographsUseCase.getFavoritesPhotographs().map { Resource.Success(it) })
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun deleteFavoritePhotograph(photo: AlbumItem) {
        viewModelScope.launch {
            getPhotographsUseCase.deleteFavoritePhotograph(photo)
            toastHelper.sendToast("photo deleted from favorites")
        }
    }

    suspend fun isPhotographFavorite(photo: AlbumItem): Boolean =
        getPhotographsUseCase.isPhotographFavorite(photo)
}