package com.gaia.public_pile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gaia.common.base.BaseViewModel
import com.gaia.common.base.ui.BaseFragment
import com.gaia.public_pile.databinding.PublicFragmentMapBinding

class MapFragment : BaseFragment<PublicFragmentMapBinding, BaseViewModel>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): PublicFragmentMapBinding {
        return PublicFragmentMapBinding.inflate(inflater)
    }

    override fun providerVMClass(): Class<BaseViewModel>? {
        return null
    }

    override fun initView() {

    }

    override fun initData() {

    }

}