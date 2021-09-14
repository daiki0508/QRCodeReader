package com.websarva.wings.android.qrcodereader.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.websarva.wings.android.qrcodereader.model.History
import com.websarva.wings.android.qrcodereader.model.SaveData

interface PreferenceHistoryRepository {
    suspend fun write(context: Context, keyName: String, data: SaveData)
    suspend fun read(context: Context, keyName: String): SaveData
    suspend fun keyList(context: Context):SaveData
    fun delete(context: Context, keyName: String)
}

class PreferenceHistoryRepositoryClient: PreferenceHistoryRepository{
    override suspend fun write(context: Context, keyName: String, data: SaveData) {
        with(createPreference(context).edit()){
            putString("${keyName}_${History.Title}", data.title)
            putInt("${keyName}_${History.Type}", data.type)
            putString("${keyName}_${History.Time}", data.time)

            // keyListを作成、保存
            if (createPreference(context).getString(History.List.name, "").isNullOrBlank()){
                putString(History.List.name, keyName)
            }else{
                putString(History.List.name, "${createPreference(context).getString(History.List.name, "")}\n${keyName}")
            }
            apply()
        }
        Log.i("preference_write", "Success!!")
    }

    override suspend fun read(context: Context, keyName: String): SaveData {
        createPreference(context).let {
            return SaveData(
                title = it.getString("${keyName}_${History.Title}", "")!!,
                type = it.getInt("${keyName}_${History.Type}", 0),
                time = it.getString("${keyName}_${History.Time}", "")!!
            )
        }
    }

    override suspend fun keyList(context: Context): SaveData {
        return SaveData(list = createPreference(context).getString(History.List.name, "")!!)
    }

    override fun delete(context: Context, keyName: String) {
        // keyListから該当履歴名を削除
        createPreference(context).let {
            val beforeKeyList = it.getString(History.List.name, "")
            var afterKeyList = beforeKeyList?.replaceFirst("$keyName\n", "")
            if (beforeKeyList == afterKeyList){
                afterKeyList = beforeKeyList?.replaceFirst(keyName, "")
            }

            // 値の更新処理
            with(it.edit()){
                putString(History.List.name, afterKeyList)
                this.remove("${keyName}_${History.Title}")
                this.remove("${keyName}_${History.Type}")
                this.remove("${keyName}_${History.Time}")
                apply()
            }
            Log.i("preference_delete", "Success")
        }
    }

    private fun createPreference(context: Context): SharedPreferences {
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            "history",
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }
}