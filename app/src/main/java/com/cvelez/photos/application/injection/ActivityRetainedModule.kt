package com.cvelez.photos.application.injection

import com.cvelez.photos.domain.DefaultPhotographsRepository
import com.cvelez.photos.domain.PhotographRepository
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
    abstract fun dataSource(impl: DefaultPhotographsRepository): PhotographRepository
}