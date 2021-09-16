package com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera

import android.os.Bundle

class BundleEvent<out: Bundle>(private val content: Bundle) {
    var hasBeenHandled = false
    private set

    val contentIfNotHandled: Bundle?
    get() {
        return if (hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    val peekContent: Bundle = content
}