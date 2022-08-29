package com.example.challengebankuish.home.services

import com.example.challengebankuish.common.models.ResultSearchModel
import com.example.challengebankuish.home.repositories.RepoRepository.Companion.ITEMS_PER_PAGE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface donde se encuentran las llamadas a la api.
 */
interface RepoService {

    /**
     * Servicio encargado de traer los repositorios de github.
     */
    @GET("search/repositories")
    suspend fun getSearchRepos(
        @Query("q") query: String? = "language:kotlin",
        @Query("per_page") perPage: Number = ITEMS_PER_PAGE,
        @Query("page") page: Number = 1
    ): Response<ResultSearchModel>
}
