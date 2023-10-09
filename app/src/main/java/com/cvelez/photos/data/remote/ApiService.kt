package com.cvelez.photos.data.remote
import com.cvelez.photos.data.model.AlbumItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(".")
    suspend fun getListPhotographsOrById(@Query("id") id: String? = null): List<AlbumItem>
}
