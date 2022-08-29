package com.example.challengebankuish.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.challengebankuish.common.models.RepoModel
import com.example.challengebankuish.home.repositories.RepoRepository
import kotlinx.coroutines.flow.Flow

/**
 * Mantiene los datos persistentes en el viewModelScope
 */
internal class RepoViewModel(repository: RepoRepository) : ViewModel() {

    val allRepoModels: Flow<PagingData<RepoModel>> = repository
        .getRepos()
        .cachedIn(viewModelScope)
}