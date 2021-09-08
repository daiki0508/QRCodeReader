package com.websarva.wings.android.qrcodereader.model

import androidx.annotation.Keep

@Keep
data class SaveData(
    var title: String = "",
    var type: Int = 0,
    var time: String = "",
    val list: String = ""
)
