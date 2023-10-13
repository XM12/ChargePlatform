package com.gaia.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gaia.common.base.BaseViewModel
import com.gaia.common.base.ui.BaseFragment
import com.gaia.forum.databinding.ForumFragmentForumBinding

class ForumFragment : BaseFragment<ForumFragmentForumBinding, BaseViewModel>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): ForumFragmentForumBinding {
        return ForumFragmentForumBinding.inflate(inflater)
    }

    override fun providerVMClass(): Class<BaseViewModel>? {
        return null
    }

    override fun initView() {

    }

    override fun initData() {

    }

}