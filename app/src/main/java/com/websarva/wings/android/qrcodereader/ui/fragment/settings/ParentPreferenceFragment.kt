package com.websarva.wings.android.qrcodereader.ui.fragment.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.websarva.wings.android.qrcodereader.BuildConfig
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment

class ParentPreferenceFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        // テーマに関する設定
        findPreference<ListPreference>("theme")?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                when(newValue.toString()){
                    "0" -> {
                        Log.i("theme", newValue.toString())
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    "1" ->{
                        Log.i("theme", newValue.toString())
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction!!.setCustomAnimations(R.anim.nav_dynamic_pop_enter_anim, R.anim.nav_dynamic_pop_exit_anim)
                transaction.replace(R.id.container, ScanFragment()).commit()

                true
            }
        }

        // アプリ評価に関する設定
        findPreference<Preference>("evaluation")?.apply {
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")

            if (uri.scheme == "https" && uri.host == "play.google.com"){
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = uri
                this.intent = intent
            }else{
                Log.e("evaluation", "不正な操作です")
                activity?.finish()
            }
        }

        // アプリの感想
        findPreference<Preference>("form")?.apply {
            val uri = Uri.parse("https://forms.gle/JuAtJ6ibFZFB8JjH9")

            if (uri.scheme == "https" && uri.host == "forms.gle"){
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = uri
                this.intent = intent
            }else{
                Log.e("evaluation", "不正な操作です")
                activity?.finish()
            }
        }

        // licenseに関する設定
        findPreference<Preference>("license")?.apply {
            setOnPreferenceClickListener {
                activity.let {
                    // TODO("未実装")
                }
                true
            }
        }

        // Policyに関する設定
        findPreference<Preference>("policy")?.apply {
            val uri = Uri.parse("https://gist.github.com/daiki0508/1d352c8808c7412be3c48ee2e41b9579")

            if (uri.scheme == "https" && uri.host == "gist.github.com"){
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = uri
                this.intent = intent
            }else{
                Log.e("evaluation", "不正な操作です")
                activity?.finish()
            }
        }
    }
}