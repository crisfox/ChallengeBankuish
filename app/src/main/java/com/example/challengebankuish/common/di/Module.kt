package com.example.challengebankuish

import com.example.challengebankuish.common.networking.Client
import com.example.challengebankuish.home.repositories.RepoRepository
import com.example.challengebankuish.home.services.RepoService
import com.example.challengebankuish.home.ui.viewmodel.RepoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Módulo del ViewModel.
 */
val viewModelModule = module {
    viewModel {
        RepoViewModel(get())
    }
}

/**
 * Módulo del repositorio.
 */
val repositoryModule = module {
    single {
        RepoRepository(get())
    }
}

/**
 * Módulo de la api.
 */
val apiModule = module {
    fun provideUseApi(retrofit: Retrofit): RepoService {
        return retrofit.create(RepoService::class.java)
    }
    single { provideUseApi(get()) }
}

/**
 * Módulo de retrofit.
 */
val retrofitModule = module {
    single { Client.provideGson }
    single { Client.okHttpClient }
    single { Client.provideRetrofit(get(), get()) }
}