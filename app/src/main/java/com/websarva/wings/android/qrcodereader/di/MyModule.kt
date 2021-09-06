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
        viewModel { CreateViewModel() }
        viewModel { HistoryViewModel(get()) }
    }
    val repository = module {
        factory { PreferenceRepositoryClient() }
    }
}