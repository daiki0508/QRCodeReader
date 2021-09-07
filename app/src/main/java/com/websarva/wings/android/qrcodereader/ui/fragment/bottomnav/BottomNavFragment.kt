package com.websarva.wings.android.qrcodereader.ui.fragment.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.websarva.wings.android.qrcodereader.R
import com.websarva.wings.android.qrcodereader.databinding.FragmentBottomnavBinding
import com.websarva.wings.android.qrcodereader.ui.fragment.create.CreateUrlFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.create.SelectFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.history.HistoryFragment
import com.websarva.wings.android.qrcodereader.ui.fragment.scan.ScanFragment
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
            val fragmentManager = it.supportFragmentManager
            binding.bottomNav.setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.scan -> {
                        if (viewModel.state().value != 0){
                            val transaction = fragmentManager.beginTransaction()
                            transaction.setCustomAnimations(R.anim.nav_dynamic_pop_enter_anim, R.anim.nav_dynamic_pop_exit_anim)
                            transaction.replace(R.id.container, ScanFragment()).commit()
                        }
                        true
                    }
                    R.id.create -> {
                        viewModel.state().value?.let { state ->
                            if (state != 2){
                                val transaction = fragmentManager.beginTransaction()
                                if (state == 0 || state == 1){
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
                        if (viewModel.state().value != 3){
                            val transaction = fragmentManager.beginTransaction()
                            transaction.setCustomAnimations(R.anim.nav_dynamic_enter_anim, R.anim.nav_dynamic_exit_anim)
                            transaction.replace(R.id.container, HistoryFragment()).commit()
                        }
                        true
                    }
                    else -> false
                }
            }
        }
    }
}