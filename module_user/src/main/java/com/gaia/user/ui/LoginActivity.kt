package com.gaia.user.ui

import com.gaia.common.base.ui.BaseActivity
import com.gaia.user.databinding.UserActivityLoginBinding
import com.gaia.user.vm.LoginVM

class LoginActivity : BaseActivity<UserActivityLoginBinding, LoginVM>() {

    override fun getViewBinding(): UserActivityLoginBinding {
        return UserActivityLoginBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<LoginVM> {
        return LoginVM::class.java
    }

    override fun initView() {

    }

    override fun initData() {

    }

}