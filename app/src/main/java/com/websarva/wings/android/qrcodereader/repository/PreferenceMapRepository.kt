package com.websarva.wings.android.qrcodereader.repository

import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.websarva.wings.android.qrcodereader.model.Map
import com.websarva.wings.android.qrcodereader.model.SaveLatLng

interface PreferenceMapRepository {
    fun write(activity: FragmentActivity, latLng: SaveLatLng)
    fun read(activity: FragmentActivity): SaveLatLng
}

class PreferenceMapRepositoryClient: PreferenceMapRepository {
    companion object{
        const val defaultLatitude: Double = 35.68
        const val defaultLongitude: Double = 139.76
    }

    override fun write(activity: FragmentActivity, latLng: SaveLatLng) {
        with(createPreference(activity).edit()){
            putLong(Map.Latitude.name, java.lang.Double.doubleToRawLongBits(latLng.latitude))
            putLong(Map.Longitude.name, java.lang.Double.doubleToRawLongBits(latLng.longitude))
            apply()
        }
    }

    override fun read(activity: FragmentActivity): SaveLatLng {
        createPreference(activity).let {
            return SaveLatLng(
                latitude = java.lang.Double.longBitsToDouble(it.getLong(Map.Latitude.name, java.lang.Double.doubleToRawLongBits(
                    defaultLatitude))),
                longitude = java.lang.Double.longBitsToDouble(it.getLong(Map.Longitude.name, java.lang.Double.doubleToRawLongBits(
                    defaultLongitude))))
        }
    }

    private fun createPreference(activity: FragmentActivity): SharedPreferences {
        val mainKey = MasterKey.Builder(activity)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            activity,
            "map",
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }
}