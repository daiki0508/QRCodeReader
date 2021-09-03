package com.websarva.wings.android.qrcodereader.di

import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MyModule {
    val module = module {
        viewModel { MainViewModel() }
    }
}