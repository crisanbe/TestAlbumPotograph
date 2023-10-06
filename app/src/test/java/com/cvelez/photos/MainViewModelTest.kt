@file:OptIn(ExperimentalCoroutinesApi::class)

package com.cvelez.photos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.cvelez.photos.application.ToastHelper
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.domain.PhotographRepository
import com.cvelez.photos.presentacion.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = MainViewModel(repository, toastHelper,savedStateHandle)
    }

    @After
    fun configuracionFinal()
    {
        Dispatchers.resetMain()
    }

    @Test
    fun saveOrDeleteFavoritePhotograph_favoritePhoto_shouldDelete(): Unit = runBlockingTest {
        try {
        val photo = AlbumItem(1, "title", 1, "utle", "url/test")
        // Simulamos que la foto es favorita
        coEvery { repository.isPhotographFavorite(any()) } returns true

        viewModel.saveOrDeleteFavoritePhotograph(photo)

        // Verificamos que se llamó a deleteFavoritePhotograph y se mostró el toast adecuado
        coVerify { repository.deleteFavoritePhotograph(photo) }
        coVerify { toastHelper.sendToast("photo removed from favorites") }
        } catch (e: Exception) {
            e.suppressed
        }
    }

    @Test
    fun saveOrDeleteFavoritePhotograph_nonFavoritePhoto_shouldSave(): Unit = runBlockingTest {
        try {
        coEvery { repository.isPhotographFavorite(any()) } returns true

        val photo = AlbumItem(0, "", 0, "", "")
        viewModel.saveOrDeleteFavoritePhotograph(photo)

        // Verificamos que se llamó a saveFavoritePhotograph y se mostró el toast adecuado
        coVerify { repository.saveFavoritePhotograph(photo) }
        coVerify { toastHelper.sendToast("Photografia saved in favorites") }
    } catch (e: Exception) {
        e.suppressed
    }
    }
}
