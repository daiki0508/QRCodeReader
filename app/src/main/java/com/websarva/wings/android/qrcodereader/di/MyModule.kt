package com.websarva.wings.android.qrcodereader.di

import com.websarva.wings.android.qrcodereader.repository.PreferenceBalloonRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.PreferenceHistoryRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.PreferenceMapRepositoryClient
import com.websarva.wings.android.qrcodereader.repository.SaveBitmapClientRepository
import com.websarva.wings.android.qrcodereader.viewmodel.bottomnav.BottomNavViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.create.DisplayViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.create.SelectViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.create.app.CreateAppsViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.create.map.CreateMapViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.create.web.CreateUrlViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.history.HistoryViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.main.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.scan.ScanViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.scan.AfterScanViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.scan.camera.CameraViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.scan.photo.PhotoViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MyModule {
    val module = module {
        viewModel { MainViewModel() }
        viewModel { BottomNavViewModel(get()) }
        viewModel { ScanViewModel(get(), get()) }
        viewModel { CameraViewModel(get()) }
        viewModel { PhotoViewModel(get()) }
        viewModel { AfterScanViewModel(get(), get()) }
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