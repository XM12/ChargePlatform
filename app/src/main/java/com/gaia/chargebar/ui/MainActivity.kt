package com.gaia.chargebar.ui

import android.os.Bundle
import com.gaia.chargebar.databinding.ActivityMainBinding
import com.gaia.chargebar.vm.MainViewModel
import com.gaia.common.base.ui.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun providerVMClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {

    }
}