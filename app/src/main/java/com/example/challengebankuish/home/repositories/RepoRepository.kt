package com.example.challengebankuish.home.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.challengebankuish.home.services.RepoService
import com.example.challengebankuish.home.sources.RepoPagingSource

/**
 * Repositorio que trae los proyectos de github.
 *
 * @property repoService RepoService para realizar el llamado
 * @constructor
 */
class RepoRepository(private val repoService: RepoService) {
    fun getRepos() = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            enablePlaceholders = false,
            maxSize = 200
        ),
        pagingSourceFactory = {
            RepoPagingSource(repoService)
        }
    ).flow

    companion object {
        const val ITEMS_PER_PAGE = 20
    }
}