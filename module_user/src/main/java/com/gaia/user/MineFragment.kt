package com.gaia.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gaia.common.base.BaseViewModel
import com.gaia.common.base.ui.BaseFragment
import com.gaia.user.databinding.UserFragmentMineBinding

class MineFragment : BaseFragment<UserFragmentMineBinding, BaseViewModel>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): UserFragmentMineBinding {
        return UserFragmentMineBinding.inflate(inflater)
    }

    override fun providerVMClass(): Class<BaseViewModel>? {
        return null
    }

    override fun initView() {

    }

    override fun initData() {

    }

}