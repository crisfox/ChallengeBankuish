package com.example.challengebankuish.home.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.challengebankuish.common.models.RepoModel
import com.example.challengebankuish.home.repositories.RepoRepository.Companion.ITEMS_PER_PAGE
import com.example.challengebankuish.home.services.RepoService

/**
 * Responsable del paginado y llamados a la api.
 */
class RepoPagingSource(private val repoService: RepoService) : PagingSource<Int, RepoModel>() {

    private var success = 0

    /**
     * Controla el paginado y casos de error, devuelve un resultado con un estado para dibujar las vistas
     * correspondientes.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoModel> {
        return try {
            val nextPageNumber = params.key ?: 1
            val result = repoService.getSearchRepos(page = nextPageNumber)

            if (result.isSuccessful) {
                with(result.body()) {
                    takeIf { (this?.totalCount ?: 0) <= 0 }?.let {
                        throw Exception("RepoViewModel: Result error")
                    }

                    success += 1
                    takeIf { success >= 4 }?.let {
                        success = 0
                        throw Exception("RepoViewModel: Result error")
                    }

                    takeIf { this?.items?.isNotEmpty() == true }?.let {
                        LoadResult.Page(
                            data = result.body()?.items ?: emptyList(),
                            prevKey = null,
                            nextKey = if (nextPageNumber >= (result.body()?.totalCount?.div(ITEMS_PER_PAGE)
                                    ?: nextPageNumber)) null else nextPageNumber + 1
                        )
                    } ?: throw Exception("RepoViewModel: Result error")
                }
            } else {
                throw Exception("RepoViewModel: ${result.errorBody()}")
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepoModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
