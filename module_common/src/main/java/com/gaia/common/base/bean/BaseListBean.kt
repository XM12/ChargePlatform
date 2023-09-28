package com.gaia.common.base.bean

data class BaseListBean<T>(
    var errorCode: Int,
    var errorMsg: String?,
    var data: MutableList<T>?
)
