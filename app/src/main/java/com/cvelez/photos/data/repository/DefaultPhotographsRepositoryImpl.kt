package com.cvelez.photos.data.repository

import androidx.lifecycle.LiveData
import com.cvelez.photos.core.Resource
import com.cvelez.photos.core.mapper.asDrinkEntity
import com.cvelez.photos.data.local.LocalDataSource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.data.model.PhotoEntity
import com.cvelez.photos.data.remote.GetServiceData
import com.cvelez.photos.domain.repository.PhotographRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ActivityRetainedScoped
class DefaultPhotographsRepositoryImpl @Inject constructor(
    private val getServiceDataUseCase: GetServiceData,
    private val localDataSource: LocalDataSource
) : PhotographRepository {

    override suspend fun getPhotographById(photoId: String): Flow<Resource<List<AlbumItem>>> = flow {
        try {
            emit(getCachedPhotographs(photoId))

            getServiceDataUseCase.getPhotographsById(photoId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        for (photo in result.data) {
                            savePhotograph(photo.asDrinkEntity())
                        }
                        emit(getCachedPhotographs(photoId))
                    }
                    is Resource.Failure -> {
                        emit(getCachedPhotographs(photoId))
                    }
                    else -> Unit
                }
            }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

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
