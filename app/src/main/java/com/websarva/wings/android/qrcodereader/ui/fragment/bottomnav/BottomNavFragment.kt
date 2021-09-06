package com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentBottomnavBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.create.CreateFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
import com.websarva.wings.android.qrcodereader.viewmodel.MainViewModel
import com.websarva.wings.android.qrcodereader.viewmodel.ScanViewModel
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

        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.scan -> {
                    if (viewModel.state().value != 0){
                        activity?.let {
                            val fragmentManager = it.supportFragmentManager
                            fragmentManager.beginTransaction().replace(R.id.container, ScanFragment()).commit()
                        }
                    }
                    true
                }
                R.id.create -> {
                    if (viewModel.state().value != 2){
                        activity?.let {
                            val fragmentManager = it.supportFragmentManager
                            fragmentManager.beginTransaction().replace(R.id.container, CreateFragment()).commit()
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }
}