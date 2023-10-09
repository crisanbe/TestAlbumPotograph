package com.cvelez.photos.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photosTable")
data class PhotoEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "albumId")
    val albumId: Int,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "url")
    val url: String = "",
    @ColumnInfo(name = "thumbnailUrl")
    val thumbnailUrl: String = "",
)

@Entity(tableName = "favoritesTable")
data class FavoritesEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "albumId")
    val albumId: Int,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "url")
    val url: String = "",
    @ColumnInfo(name = "thumbnailUrl")
    val thumbnailUrl: String = "",
    )
