package com.gaia.chargebar.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import com.gaia.basic.media.FloatingWindow
import com.gaia.basic.media.VideoService
import com.gaia.chargebar.R
import com.gaia.chargebar.databinding.ActivityMainBinding
import com.gaia.chargebar.vm.MainVM
import com.gaia.common.base.ui.BaseActivity
import com.gaia.forum.ForumFragment
import com.gaia.personal_pile.PersonalPileFragment
import com.gaia.public_pile.MapFragment
import com.gaia.user.MineFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : BaseActivity<ActivityMainBinding, MainVM>() {
    private lateinit var mapfragment: MapFragment
    private lateinit var forumFragment: ForumFragment
    private lateinit var personalPileFragment: PersonalPileFragment
    private lateinit var mineFragment: MineFragment

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<MainVM> {
        return MainVM::class.java
    }

    override fun initView() {
        toolBar.visibility = View.GONE
        val navView: BottomNavigationView = binding.navView
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> switchFragment(mapfragment)
                R.id.navigation_forum -> switchFragment(forumFragment)
                R.id.navigation_pile -> switchFragment(personalPileFragment)
                R.id.navigation_mine -> switchFragment(mineFragment)
            }
            return@setOnItemSelectedListener true
        }
    }

    @SuppressLint("CheckResult")
    override fun initData() {
        mapfragment = MapFragment()
        forumFragment = ForumFragment()
        personalPileFragment = PersonalPileFragment()
        mineFragment = MineFragment()
        switchFragment(mapfragment)

        val floatingWindow = FloatingWindow(this)
        floatingWindow.initWindow()
    }

    private fun switchFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        for (cacheFragment in manager.fragments) {
            if (cacheFragment.isVisible) {
                transaction.hide(cacheFragment)
            }
        }
        if (!fragment.isAdded) {
            transaction.add(R.id.nav_host_fragment_activity_main, fragment)
        }
        transaction.show(fragment)
        transaction.commit()
    }
}