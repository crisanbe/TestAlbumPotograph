package com.cvelez.photos.core.mapper

import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.data.model.FavoritesEntity
import com.cvelez.photos.data.model.PhotoEntity

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