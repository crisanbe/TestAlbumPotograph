package com.cvelez.photos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.cvelez.photos.utils.ToastHelper
import com.cvelez.photos.core.Resource
import com.cvelez.photos.data.model.AlbumItem
import com.cvelez.photos.data.remote.ApiService
import com.cvelez.photos.domain.usecase.GetPhotographsUseCase
import com.cvelez.photos.ui.viewmodel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getPhotographsUseCase: GetPhotographsUseCase

    @RelaxedMockK
    lateinit var toastHelper: ToastHelper

    @RelaxedMockK
    lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: MainViewModel
    private var service = retrofit.create(ApiService::class.java)
    companion object{
        private lateinit var retrofit: Retrofit

        @BeforeClass
        @JvmStatic
        fun setupCommon(){
            retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/photos/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        savedStateHandle.set("1", "someValue")
        viewModel = MainViewModel(getPhotographsUseCase, toastHelper,savedStateHandle)
        service = retrofit.create(ApiService::class.java)
    }

    @Test
    fun checkThatTheListWasConsumed(){
        runBlocking {
            val result = service.getListPhotographsOrById()
            assertThat(result, `is`(notNullValue()))
        }
    }

    @Test
    fun checkThatThePhotoWasObtainedById(){
        runBlocking {
            val result = service.getListPhotographsOrById("1")
            assertThat(result, `is`(notNullValue()))
        }
    }

    @Test
    fun `fetchPhotographsList should emit Loading and Success`() {
        runBlocking {
            // Given
            val mockPhotograph = AlbumItem(1, "title", 1, "htpps://url", "https://test")
            coEvery { getPhotographsUseCase.getPhotographById("1") } returns flowOf(Resource.Success(listOf(mockPhotograph) ))

            // When
            viewModel.setPhotograph("1")

            // Then
            viewModel.fetchPhotographsList.observeForever {
                assert(it is Resource.Loading || it is Resource.Success)
            }
        }
    }
}


