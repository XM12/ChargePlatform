package com.gaia.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gaia.common.base.BaseViewModel
import com.gaia.common.base.ui.BaseFragment
import com.gaia.user.databinding.UserFragmentMineBinding

class MineFragment : BaseFragment<UserFragmentMineBinding, BaseViewModel>(), View.OnClickListener {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): UserFragmentMineBinding {
        return UserFragmentMineBinding.inflate(inflater)
    }

    override fun providerVMClass(): Class<BaseViewModel>? {
        return null
    }

    override fun initView() {
        binding.imgAvatar.setOnClickListener(this)
        binding.layoutPersonalBalance.setOnClickListener(this)
        binding.layoutEnterpriseBalance.setOnClickListener(this)
        binding.layoutRecharge.setOnClickListener(this)
        binding.layoutOrder.setOnClickListener(this)
        binding.layoutCards.setOnClickListener(this)
        binding.layoutInvoice.setOnClickListener(this)
        binding.layoutVehicle.setOnClickListener(this)
        binding.layoutPaySetting.setOnClickListener(this)
        binding.layoutRefund.setOnClickListener(this)
        binding.layoutMessage.setOnClickListener(this)
        binding.layoutCustomerService.setOnClickListener(this)
        binding.layoutSetting.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.img_avatar -> {

            }

            R.id.layout_personal_balance -> {

            }

            R.id.layout_enterprise_balance -> {

            }

            R.id.layout_recharge -> {

            }

            R.id.layout_order -> {

            }

            R.id.layout_cards -> {

            }

            R.id.layout_invoice -> {

            }

            R.id.layout_vehicle -> {

            }

            R.id.layout_pay_setting -> {

            }

            R.id.layout_refund -> {

            }

            R.id.layout_message -> {

            }

            R.id.layout_customer_service -> {

            }

            R.id.layout_setting -> {

            }
        }
    }

}