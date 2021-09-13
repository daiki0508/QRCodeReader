package com.websarva.wings.android.qrcodereader.repository

import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.websarva.wings.android.qrcodereader.model.Balloon

interface PreferenceBalloonRepository {
    fun save(activity: FragmentActivity)
    fun read(activity: FragmentActivity): Boolean
}

class PreferenceBalloonRepositoryClient: PreferenceBalloonRepository {
    override fun save(activity: FragmentActivity) {
        with(createPreference(activity).edit()){
            putBoolean(Balloon.Balloon.name, false)
            apply()
        }
    }

    override fun read(activity: FragmentActivity): Boolean {
        return createPreference(activity).getBoolean(Balloon.Balloon.name, true)
    }

    private fun createPreference(activity: FragmentActivity): SharedPreferences {
        val mainKey = MasterKey.Builder(activity)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            activity,
            "balloon",
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }
}