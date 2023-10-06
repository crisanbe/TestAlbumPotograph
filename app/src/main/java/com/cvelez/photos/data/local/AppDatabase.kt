package com.cvelez.photos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cvelez.photos.data.model.FavoritesEntity
import com.cvelez.photos.data.model.PhotoEntity

@Database(entities = [FavoritesEntity::class, PhotoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photographDao(): PhotographDao
}