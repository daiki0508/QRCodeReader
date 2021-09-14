package com.websarva.wings.android.qrcodereader.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.skydoves.balloon.showAlignTop
import com.websarva.wings.android.qrcodereader.databinding.FragmentSettingsBinding
import com.websarva.wings.android.qrcodereader.viewmodel.BottomNavViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding
    get() = _binding!!

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val bottomNavViewModel by activityViewModels<BottomNavViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初期設定
        mainViewModel.setState(4)

        // balloonの表示
        bottomNavViewModel.let {
            it.bottomNavView().value!!.showAlignTop(it.bottomNavBalloonSettings().value!!)
        }
    }
}