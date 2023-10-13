package com.gaia.common.base.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gaia.common.base.BaseViewModel

abstract class BaseFragment<T : ViewBinding, VM : BaseViewModel>  : Fragment() {
    protected lateinit var context: Activity
    protected lateinit var binding: T
    protected lateinit var viewModel: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this)[it]
        }
        binding = getViewBinding(inflater, container, savedInstanceState)
        initView()
        initData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T

    abstract fun providerVMClass(): Class<VM>?

    protected abstract fun initView()

    protected abstract fun initData()

}