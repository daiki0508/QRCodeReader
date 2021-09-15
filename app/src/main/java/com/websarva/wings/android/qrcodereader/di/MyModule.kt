package com.websarva.wings.android.qrcodereader.di

import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.PreferenceHistoryRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.PreferenceMapRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.SaveBitmapClientRepository
import com.websarva.wings.android.qrcodereader.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MyModule {
    val module = module {
        viewModel { MainViewModel() }
        viewModel { BottomNavViewModel(get()) }
        viewModel { ScanViewModel(get(), get()) }
        viewModel { CameraViewModel(get(), get()) }
        viewModel { PhotoViewModel(get()) }
        viewModel { AfterScanViewModel() }
        viewModel { SelectViewModel(get(), get()) }
        viewModel { CreateUrlViewModel() }
        viewModel { HistoryViewModel(get(), get(), get()) }
        viewModel { CreateMapViewModel(get(), get()) }
        viewModel { CreateAppsViewModel(get()) }
        viewModel { DisplayViewModel(get(), get()) }
        viewModel { SettingsViewModel(get(), get()) }
    }
    val repository = module {
        factory { PreferenceHistoryRepositoryClient() }
        factory { SaveBitmapClientRepository() }
        factory { PreferenceMapRepositoryClient() }
        factory { PreferenceBalloonRepositoryClient() }
    }
}