@file:Suppress("DEPRECATION")

package com.cvelez.photos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.cvelez.photos.application.ToastHelper
import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.domain.PhotographRepository
import com.cvelez.photos.presentacion.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var repository: PhotographRepository

    @RelaxedMockK
    lateinit var toastHelper: ToastHelper

    @RelaxedMockK
    lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        savedStateHandle.set("1", "someValue")
        viewModel = MainViewModel(repository, toastHelper,savedStateHandle)
    }

    @Test
    fun `fetchPhotographsList should emit Loading and Success`() {
        runBlocking {
            // Given
            val mockPhotograph = AlbumItem(1, "title", 1, "utle", "url/test")
            coEvery { repository.getPhotographById("1") } returns flowOf()

            // When
            viewModel.setPhotograph("1")

            // Then
            viewModel.fetchPhotographsList.observeForever {
                assert(it is Resource.Loading || it is Resource.Success)
            }
        }
    }
}


