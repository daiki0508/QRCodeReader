package com.websarva.wings.android.qrcodereader.ui.fragment.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.websarva.wings.android.qrcodereader.R

class ParentPreferenceFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }
}