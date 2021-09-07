package com.websarva.wings.android.qrcodereader.di

import com.websarva.wings.android.qrcodereader.repository.PreferenceRepositoryClient
import com.websarva.wings.android.qrcodereader.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MyModule {
    val module = module {
        viewModel { MainViewModel() }
        viewModel { ScanViewModel(get()) }
        viewModel { AfterScanViewModel() }
        viewModel { CreateUrlViewModel() }
        viewModel { HistoryViewModel(get()) }
        viewModel { CreateMapViewModel() }
        viewModel { DisplayViewModel() }
    }
    val repository = module {
        factory { PreferenceRepositoryClient() }
    }
}