package com.pinyou.basic.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue


object ScreenUtils {

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getStatusBarHeight(context: Context): Int {
        var height = dp2px(30f)
        val resourceId = context.applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = context.applicationContext.resources.getDimensionPixelSize(resourceId)
        }
        return height
    }

    fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics).toInt()
    }

    fun px2dp(px: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

}