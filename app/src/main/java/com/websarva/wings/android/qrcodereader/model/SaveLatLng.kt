package com.websarva.wings.android.qrcodereader.model

import androidx.annotation.Keep

@Keep
data class SaveLatLng(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
