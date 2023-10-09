package com.cvelez.photos.data.remote

import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetServiceData @Inject constructor(
    private val webService: ApiService
) {
    suspend fun getPhotographsById(id: String): Flow<Resource<List<AlbumItem>>> = flow {
        try {
            val photographs = webService.getListPhotographsOrById()
            emit(Resource.Success(photographs))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
