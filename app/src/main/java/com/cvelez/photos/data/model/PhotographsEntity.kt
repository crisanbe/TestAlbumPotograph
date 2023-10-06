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

fun List<FavoritesEntity>.asDrinkLists(): List<AlbumItem> = this.map {
    AlbumItem(it.id,it.title, it.albumId, it.thumbnailUrl, it.url)
}

fun List<PhotoEntity>.asDrinkList(): List<AlbumItem> = this.map {
    AlbumItem(it.id, it.title, it.albumId, it.thumbnailUrl, it.url)
}

fun AlbumItem.asDrinkEntity(): PhotoEntity =
    PhotoEntity(this.id, this.albumId, this.title, this.thumbnailUrl, this.url)

fun AlbumItem.asFavoriteEntity(): FavoritesEntity =
    FavoritesEntity(this.id, this.albumId, this.title, this.thumbnailUrl, this.url)