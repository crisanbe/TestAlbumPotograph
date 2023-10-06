package com.cvelez.photos.domain

import androidx.lifecycle.LiveData
import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.data.model.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotographRepository {
    suspend fun getPhotographById(photoId: String): Flow<Resource<List<AlbumItem>>>
    suspend fun saveFavoritePhotograph(photo: AlbumItem)
    suspend fun isPhotographFavorite(photo: AlbumItem): Boolean
    suspend fun getCachedPhotographs(photoId: String): Resource<List<AlbumItem>>
    suspend fun savePhotograph(photo: PhotoEntity)
    fun getFavoritesPhotographs(): LiveData<List<AlbumItem>>
    suspend fun deleteFavoritePhotograph(photo: AlbumItem)
}