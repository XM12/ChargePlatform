package com.pinyou.personal_pile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pinyou.common.base.BaseViewModel
import com.pinyou.common.base.ui.BaseFragment
import com.pinyou.personal_pile.databinding.PileFragmentPileBinding

class PersonalPileFragment : BaseFragment<PileFragmentPileBinding, BaseViewModel>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): PileFragmentPileBinding {
        return PileFragmentPileBinding.inflate(inflater)
    }

    override fun providerVMClass(): Class<BaseViewModel>? {
        return null
    }

    override fun initView() {

    }

    override fun initData() {

    }

}