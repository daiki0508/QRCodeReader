package com.websarva.wings.android.qrcodereader.di

import com.websarva.wings.android.qrcodereader.repository.PreferenceRepositoryClient
import com.websarva.wings.android.qrcodereader.viewmodel.AfterScanViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.CreateViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.ScanViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MyModule {
    val module = module {
        viewModel { MainViewModel() }
        viewModel { ScanViewModel(get()) }
        viewModel { AfterScanViewModel() }
        viewModel { CreateViewModel() }
    }
    val repository = module {
        factory { PreferenceRepositoryClient() }
    }
}