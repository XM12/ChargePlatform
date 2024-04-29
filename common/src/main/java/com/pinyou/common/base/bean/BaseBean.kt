package com.pinyou.common.base.bean

data class BaseBean<T>(
    var errorCode: Int,
    var errorMsg: String?,
    var data: T?
)
