package com.cvelez.photos.domain.usecase

import androidx.lifecycle.LiveData
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.domain.repository.PhotographRepository
import javax.inject.Inject

class GetPhotographsUseCase @Inject constructor(private val repository: PhotographRepository) {

     suspend fun getPhotographById(photoId: String) = repository.getPhotographById(photoId)

     suspend fun saveFavoritePhotograph(photo: AlbumItem) = repository.saveFavoritePhotograph(photo)

     suspend fun isPhotographFavorite(photo: AlbumItem): Boolean = repository.isPhotographFavorite(photo)

     fun getFavoritesPhotographs(): LiveData<List<AlbumItem>> = repository.getFavoritesPhotographs()

     suspend fun deleteFavoritePhotograph(photo: AlbumItem) = repository.deleteFavoritePhotograph(photo)
}
