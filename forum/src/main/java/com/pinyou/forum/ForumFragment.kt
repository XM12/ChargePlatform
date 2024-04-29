package com.pinyou.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pinyou.common.base.BaseViewModel
import com.pinyou.common.base.ui.BaseFragment
import com.pinyou.forum.databinding.ForumFragmentForumBinding

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