package com.example.challengebankuish

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Error
import androidx.paging.PagingSource.LoadResult.Page
import com.example.challengebankuish.common.models.RepoModel
import com.example.challengebankuish.common.models.ResultSearchModel
import com.example.challengebankuish.home.sources.RepoPagingSource
import com.example.challengebankuish.home.services.RepoService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class RepoPagingSourceTest {

    private lateinit var source: RepoPagingSource
    private val service: RepoService = mock()

    @Before
    fun `set up`() {
        source = RepoPagingSource(service)
    }

    @Test
    fun `getSearchRepos - should have three items`() = runTest {
        // Given
        val resultSearchModel = ResultSearchModel(
            totalCount = 20,
            items = listOf(
                RepoModel(id = 1),
                RepoModel(id = 2),
                RepoModel(id = 3)
            )
        )
        // When
        whenever(service.getSearchRepos()).thenReturn(Response.success(resultSearchModel))
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )
        // Then
        assertEquals(
            Page(
                data = listOf(resultSearchModel.items[0], resultSearchModel.items[1], resultSearchModel.items[2]),
                prevKey = null,
                nextKey = resultSearchModel.items[1].name
            ),
            result
        )
        verify(service).getSearchRepos()
    }

    @Test
    fun `paging source refresh success`() = runTest {
        // Given
        val resultSearchModel = ResultSearchModel(
            totalCount = 20,
            items = listOf(
                RepoModel(id = 1),
                RepoModel(id = 2),
                RepoModel(id = 3)
            )
        )
        // When
        whenever(service.getSearchRepos(any(), any(), any())).thenReturn(Response.success(resultSearchModel))
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val expectedResult = Page(
            data = resultSearchModel.items.map { RepoModel(id = it.id) },
            prevKey = null,
            nextKey = 1
        )
        // Then
        assertEquals(
            expectedResult,
            result
        )
        verify(service).getSearchRepos(any(), any(), any())
    }

    @Test
    fun `paging source append success`() = runTest {
        // Given
        val resultSearchModel = ResultSearchModel(
            totalCount = 200,
            items = listOf(
                RepoModel(id = 1),
                RepoModel(id = 2),
                RepoModel(id = 3)
            )
        )
        // When
        whenever(service.getSearchRepos(any(), any(), any())).thenReturn(Response.success(resultSearchModel))
        val result = source.load(
            PagingSource.LoadParams.Append(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        val expectedResult = Page(
            data = resultSearchModel.items.map { RepoModel(id = it.id) },
            prevKey = null,
            nextKey = 2
        )
        // Then
        assertEquals(
            expectedResult,
            result
        )
        verify(service).getSearchRepos(any(), any(), any())
    }

    @Test
    fun `getSearchRepos - with empty items return error`() = runTest {
        // Given
        val resultSearchModel = ResultSearchModel(
            totalCount = 20,
            items = emptyList()
        )
        // When
        whenever(service.getSearchRepos()).thenReturn(Response.success(resultSearchModel))
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )
        val expectedResult = Error<Int, RepoModel>(Exception("RepoViewModel: Result error"))
        // Then
        assertEquals(
            expectedResult.toString(),
            result.toString()
        )
        verify(service).getSearchRepos()
    }

    @Test
    fun `getSearchRepos - totalCount null should return error`() = runTest {
        // Given
        val resultSearchModel = ResultSearchModel(
            totalCount = null,
            items = listOf(
                RepoModel(id = 1),
                RepoModel(id = 2),
                RepoModel(id = 3)
            )
        )
        // When
        whenever(service.getSearchRepos()).thenReturn(Response.success(resultSearchModel))
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )
        val expectedResult = Error<Int, RepoModel>(Exception("RepoViewModel: Result error"))
        // Then
        assertEquals(
            expectedResult.toString(),
            result.toString()
        )
        verify(service).getSearchRepos()
    }

    @Test
    fun `getSearchRepos - throw Runtime should return error`() = runTest {
        // When
        val error = RuntimeException("404", Throwable())
        given(service.getSearchRepos()).willThrow(error)
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        val expectedResult = Error<Int, RepoModel>(error)
        // Then
        assertEquals(
            expectedResult.toString(),
            result.toString()
        )
        verify(service).getSearchRepos()
    }

    @Test
    fun `getSearchRepos - return null should return error`() = runTest {
        // When
        given(service.getSearchRepos()).willReturn(null)
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        val expectedResult = Error<Int, RepoModel>(NullPointerException())
        // Then
        assertEquals(
            expectedResult.toString(),
            result.toString()
        )
        verify(service).getSearchRepos()
    }

    @Test
    fun `getSearchRepos - isSuccessful in false should return error`() = runTest {
        // Given
        val responseBodyMock: ResponseBody = mock()
        val errorResponse: Response<ResultSearchModel> = Response.error(404, responseBodyMock)
        // When
        whenever(service.getSearchRepos()).thenReturn(errorResponse)
        // When
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        val expectedResult = Error<Int, RepoModel>(Exception("RepoViewModel: ${errorResponse.errorBody()}"))
        // Then
        assertEquals(
            expectedResult.toString(),
            result.toString()
        )
        verify(service).getSearchRepos()
    }
}
