package com.websarva.wings.android.qrcodereader.di

import com.websarva.wings.android.qrcodereader.repository.PreferenceHistoryRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.PreferenceMapRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.SaveBitmapClientRepository
import com.websarva.wings.android.qrcodereader.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MyModule {
    val module = module {
        viewModel { MainViewModel() }
        viewModel { ScanViewModel() }
        viewModel { CameraViewModel(get()) }
        viewModel { PhotoViewModel() }
        viewModel { AfterScanViewModel() }
        viewModel { CreateUrlViewModel() }
        viewModel { HistoryViewModel(get()) }
        viewModel { CreateMapViewModel(get()) }
        viewModel { CreateAppsViewModel() }
        viewModel { DisplayViewModel(get()) }
    }
    val repository = module {
        factory { PreferenceHistoryRepositoryClient() }
        factory { SaveBitmapClientRepository() }
        factory { PreferenceMapRepositoryClient() }
    }
}