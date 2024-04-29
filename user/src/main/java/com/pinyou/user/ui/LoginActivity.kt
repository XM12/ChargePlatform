package com.pinyou.user.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.pinyou.basic.route.path.RouteUserPath
import com.pinyou.common.base.ui.BaseActivity
import com.pinyou.user.databinding.UserActivityLoginBinding
import com.pinyou.user.vm.LoginVM

@Route(path = RouteUserPath.LOGIN_ACTIVITY, group = RouteUserPath.PACKAGE_NAME)
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