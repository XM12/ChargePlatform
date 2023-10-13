package com.gaia.chargebar.ui

import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.gaia.chargebar.R
import com.gaia.chargebar.databinding.ActivityMainBinding
import com.gaia.chargebar.vm.MainVM
import com.gaia.common.base.ui.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity<ActivityMainBinding, MainVM>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<MainVM> {
        return MainVM::class.java
    }

    override fun initView() {
        toolBar.visibility = View.GONE
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_forum, R.id.navigation_pile, R.id.navigation_mine))
        toolBar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun initData() {

    }
}