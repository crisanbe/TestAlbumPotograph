package com.cvelez.photos.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.cvelez.photos.data.model.*
import com.cvelez.photos.core.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
class LocalDataSource @Inject constructor(private val photographDao: PhotographDao) {

    suspend fun saveFavoritePhotograph(photo: AlbumItem) {
        return photographDao.saveFavoritePhotograph(photo.asFavoriteEntity())
    }

    fun getFavoritesPhotographs(): LiveData<List<AlbumItem>> {
        return photographDao.getAllFavoritePhotographsWithChanges().map { it.asDrinkLists() }
    }

    suspend fun deletePhotograph(photo: AlbumItem) {
        return photographDao.deleteFavoritePhotograph(photo.asFavoriteEntity())
    }

    suspend fun savePhotographs(photo: PhotoEntity) {
        photographDao.savePhotograph(photo)
    }

    suspend fun getCachedPhotographs(photo: String): Resource<List<AlbumItem>> {
        return Resource.Success(photographDao.getPhotographs(photo).asDrinkList())
    }

    suspend fun isPhotographFavorite(photo: AlbumItem): Boolean {
        return photographDao.getPhotographById(photo.id) != null
    }
}