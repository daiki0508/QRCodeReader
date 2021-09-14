package com.websarva.wings.android.qrcodereader.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.websarva.wings.android.qrcodereader.model.Balloon

interface PreferenceBalloonRepository {
    fun save(context: Context)
    fun read(context: Context): Boolean
}

class PreferenceBalloonRepositoryClient: PreferenceBalloonRepository {
    override fun save(context: Context) {
        with(createPreference(context).edit()){
            putBoolean(Balloon.Balloon.name, false)
            apply()
        }
    }

    override fun read(context: Context): Boolean {
        return createPreference(context).getBoolean(Balloon.Balloon.name, true)
    }

    private fun createPreference(context: Context): SharedPreferences {
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            "balloon",
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }
}