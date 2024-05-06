package com.pinyou.chargebar.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import com.pinyou.chargebar.R
import com.pinyou.chargebar.databinding.ActivityMainBinding
import com.pinyou.chargebar.vm.MainVM
import com.pinyou.common.base.ui.BaseActivity
import com.pinyou.forum.ForumFragment
import com.pinyou.personal_pile.PersonalPileFragment
import com.pinyou.public_pile.MapFragment
import com.pinyou.user.MineFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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
//        mineFragment = MineFragment()
        switchFragment(mapfragment)
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