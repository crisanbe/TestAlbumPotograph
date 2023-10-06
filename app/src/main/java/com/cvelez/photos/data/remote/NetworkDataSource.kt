package com.cvelez.photos.data.remote

import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(
    private val webService: WebService
) {
    suspend fun getPhotographsById(id: String): Flow<Resource<List<AlbumItem>>> =
        callbackFlow {
            trySend(
                Resource.Success(
                    webService.getListPhotographsOrById()
                )
            ).isSuccess
            awaitClose { close() }
        }
}
