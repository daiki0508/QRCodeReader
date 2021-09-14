package com.websarva.wings.android.qrcodereader.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import com.websarva.wings.android.qrcodereader.model.CacheName
import java.io.File
import java.io.FileOutputStream

interface SaveBitmapClient {
    suspend fun save(context: Context, qrcode: Bitmap)
    fun delete(context: Context)
}

class SaveBitmapClientRepository: SaveBitmapClient {
    override suspend fun save(context: Context, qrcode: Bitmap) {
        val file = File(context.cacheDir, CacheName().cache)

        FileOutputStream(file).use {
            qrcode.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.flush()
        }
    }

    override fun delete(context: Context) {
        val file = File(context.cacheDir, CacheName().cache)

        FileOutputStream(file).use {
            file.delete()
        }
    }
}