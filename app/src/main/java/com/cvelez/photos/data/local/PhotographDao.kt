package com.cvelez.photos.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cvelez.photos.data.model.FavoritesEntity
import com.cvelez.photos.data.model.PhotoEntity

@Dao
interface PhotographDao {

    @Query("SELECT * FROM favoritesTable")
    suspend fun getAllFavoritePhotographs(): List<FavoritesEntity>

    @Query("SELECT * FROM favoritesTable")
    fun getAllFavoritePhotographsWithChanges(): LiveData<List<FavoritesEntity>>

    @Query("SELECT * FROM photosTable WHERE id LIKE '%' || :photoId || '%'")
    suspend fun getPhotographs(photoId: String): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoritePhotograph(photo: FavoritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePhotograph(photo: PhotoEntity)

    @Delete
    suspend fun deleteFavoritePhotograph(drink: FavoritesEntity)

    @Query("SELECT * FROM favoritesTable WHERE id = :photoId")
    suspend fun getPhotographById(photoId: Int): FavoritesEntity?

}