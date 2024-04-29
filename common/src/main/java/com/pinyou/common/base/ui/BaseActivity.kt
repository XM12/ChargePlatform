package com.pinyou.common.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.pinyou.basic.utils.StatusBarUtils
import com.pinyou.common.base.BaseViewModel
import com.pinyou.common.databinding.CommonActivityBaseBinding
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseActivity<T : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    private val TAG = "BaseActivity"
    protected lateinit var binding: T
    protected var viewModel: VM? = null
    protected lateinit var toolBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseBinding = CommonActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBinding.root)

        StatusBarUtils.transparentStatusBar(this)

        toolBar = baseBinding.toolbar
        binding = getViewBinding()
        baseBinding.rootView.addView(binding.root)

        providerVMClass()?.let {
            viewModel = ViewModelProvider(this)[it]
        }

        initView()
        initData()
    }

    abstract fun getViewBinding(): T

    protected abstract fun providerVMClass(): Class<VM>?

    protected abstract fun initView()

    protected abstract fun initData()


}