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
    suspend fun read(activity: FragmentActivity, keyName: String): SaveData
    suspend fun keyList(activity: FragmentActivity):SaveData
}

class PreferenceRepositoryClient: PreferenceRepository{
    override suspend fun write(activity: FragmentActivity, keyName: String, data: SaveData) {
        with(createPreference(activity).edit()){
            putString("${keyName}_${History.Title}", data.title)
            putInt("${keyName}_${History.Type}", data.type)
            putString("${keyName}_${History.Time}", data.time)

            // keyListを作成、保存
            if (createPreference(activity).getString(History.List.name, "").isNullOrBlank()){
                putString(History.List.name, keyName)
            }else{
                putString(History.List.name, "${createPreference(activity).getString(History.List.name, "")}\n${keyName}")
            }
            apply()
        }
        Log.i("preference_write", "Success!!")
    }

    override suspend fun read(activity: FragmentActivity, keyName: String): SaveData {
        createPreference(activity).let {
            return SaveData(
                title = it.getString("${keyName}_${History.Title}", "")!!,
                type = it.getInt("${keyName}_${History.Type}", 0),
                time = it.getString("${keyName}_${History.Time}", "")!!
            )
        }
    }

    override suspend fun keyList(activity: FragmentActivity): SaveData {
        return SaveData(list = createPreference(activity).getString(History.List.name, "")!!)
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