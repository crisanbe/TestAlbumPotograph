package com.cvelez.photos.application.injection

import com.cvelez.photos.data.repository.DefaultPhotographsRepositoryImpl
import com.cvelez.photos.domain.repository.PhotographRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {
    @Binds
    abstract fun dataSource(impl: DefaultPhotographsRepositoryImpl): PhotographRepository
}