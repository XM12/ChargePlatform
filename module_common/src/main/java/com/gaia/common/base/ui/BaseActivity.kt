package com.gaia.common.base.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gaia.common.base.BaseViewModel
import com.gaia.common.databinding.ActivityBaseBinding
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseActivity<T : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    private val TAG = "BaseActivity"
    protected lateinit var binding: T
    protected var viewModel: VM? = null
    protected lateinit var toolBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBinding.root)
        toolBar = baseBinding.toolbar
        binding = getViewBinding()
        baseBinding.rootView.addView(binding.root)
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this)[it]
        }
        baseBinding.tvTitle.text = title
        initView()
    }

    abstract fun providerVMClass(): Class<VM>?

    abstract fun getViewBinding(): T

    abstract fun initView()

    @Composable
    fun TitleBar() {

    }
}