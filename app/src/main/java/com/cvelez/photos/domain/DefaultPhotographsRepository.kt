package com.cvelez.photos.domain

import androidx.lifecycle.LiveData
import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.local.LocalDataSource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.data.model.PhotoEntity
import com.cvelez.photos.data.model.asDrinkEntity
import com.cvelez.photos.data.remote.NetworkDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ActivityRetainedScoped
class DefaultPhotographsRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : PhotographRepository {

    override suspend fun getPhotographById(photoId: String): Flow<Resource.Success<List<AlbumItem>>> =
        callbackFlow {

            trySend(getCachedPhotographs(photoId)).isSuccess

            networkDataSource.getPhotographsById(photoId).collect {
                when (it) {
                    is Resource.Success -> {
                        for (drink in it.data) {
                            savePhotograph(drink.asDrinkEntity())
                        }
                        trySend(getCachedPhotographs(photoId)).isSuccess
                    }
                    is Resource.Failure -> {
                        trySend(getCachedPhotographs(photoId)).isSuccess
                    }
                    else -> Unit
                }
            }
            awaitClose { cancel() }
        }

    override suspend fun saveFavoritePhotograph(photo: AlbumItem) {
        localDataSource.saveFavoritePhotograph(photo)
    }

    override suspend fun isPhotographFavorite(photo: AlbumItem): Boolean =
        localDataSource.isPhotographFavorite(photo)

    override suspend fun savePhotograph(photo: PhotoEntity) {
        localDataSource.savePhotographs(photo)
    }

    override fun getFavoritesPhotographs(): LiveData<List<AlbumItem>> {
        return localDataSource.getFavoritesPhotographs()
    }

    override suspend fun deleteFavoritePhotograph(photo: AlbumItem) {
        localDataSource.deletePhotograph(photo)
    }

    override suspend fun getCachedPhotographs(photoId: String): Resource.Success<List<AlbumItem>> {
        return localDataSource.getCachedPhotographs(photoId)
    }
}