package com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentBottomnavBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.history.HistoryFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.camera.CameraFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.settings.SettingsFragment
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BottomNavFragment: Fragment() {
    private var _binding: FragmentBottomnavBinding? = null
    private val binding
    get() = _binding!!
    private val viewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomnavBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            binding.bottomNav.setOnItemSelectedListener { item ->
                val transaction = it.supportFragmentManager.beginTransaction()

                when(item.itemId){
                    R.id.scan -> {
                        if (viewModel.state().value != 0){
                            transaction.setCustomAnimations(R.anim.nav_dynamic_pop_enter_anim, R.anim.nav_dynamic_pop_exit_anim)
                            transaction.replace(R.id.container, ScanFragment()).commit()
                        }
                        true
                    }
                    R.id.create -> {
                        viewModel.state().value?.let { state ->
                            if (state != 2){
                                if (state == 0){
                                    transaction.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                                }else{
                                    transaction.setCustomAnimations(R.anim.nav_dynamic_pop_enter_anim, R.anim.nav_dynamic_pop_exit_anim)
                                }
                                transaction.replace(R.id.container, SelectFragment()).commit()
                            }
                        }
                        true
                    }
                    R.id.history ->{
                        viewModel.state().value?.let { state ->
                            if (state != 3){
                                if (state == 4){
                                    transaction.setCustomAnimations(R.anim.nav_dynamic_pop_enter_anim, R.anim.nav_dynamic_pop_exit_anim)
                                }else{
                                    transaction.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                                }
                                transaction.replace(R.id.container, HistoryFragment()).commit()
                            }
                        }
                        true
                    }
                    R.id.settings -> {
                        if (viewModel.state().value != 4){
                            transaction.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                            transaction.replace(R.id.container, SettingsFragment()).commit()
                        }
                        true
                    }
                    else -> false
                }
            }
        }
    }
}