package com.websarva.wings.android.qrcodereader.repository

import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import com.websarva.wings.android.qrcodereader.model.CacheName
import java.io.File
import java.io.FileOutputStream

interface SaveBitmapClient {
    suspend fun save(activity: FragmentActivity, qrcode: Bitmap)
    fun delete(activity: FragmentActivity)
}

class SaveBitmapClientRepository: SaveBitmapClient {
    override suspend fun save(activity: FragmentActivity, qrcode: Bitmap) {
        val file = File(activity.cacheDir, CacheName().cache)

        FileOutputStream(file).use {
            qrcode.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.flush()
        }
    }

    override fun delete(activity: FragmentActivity) {
        val file = File(activity.cacheDir, CacheName().cache)

        FileOutputStream(file).use {
            file.delete()
        }
    }
}