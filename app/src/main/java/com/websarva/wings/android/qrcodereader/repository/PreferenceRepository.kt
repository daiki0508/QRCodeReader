package com.websarva.wings.android.qrcodereader.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.websarva.wings.android.qrcodereader.model.History
import com.websarva.wings.android.qrcodereader.model.SaveData

interface PreferenceRepository {
    suspend fun write(activity: FragmentActivity, keyName: String, data: SaveData)
}

class PreferenceRepositoryClient: PreferenceRepository{
    override suspend fun write(activity: FragmentActivity, keyName: String, data: SaveData) {
        with(createPreference(activity).edit()){
            putString("${keyName}_${History.Title}", data.title)
            putInt("${keyName}_${History.Type}", data.type)
            putString("${keyName}_${History.Time}", data.time)
            apply()
        }
        Log.i("preference_write", "Success!!")
    }

    private fun createPreference(activity: FragmentActivity): SharedPreferences {
        val mainKey = MasterKey.Builder(activity)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            activity,
            "history",
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }
}