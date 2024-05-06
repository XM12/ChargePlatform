package com.pinyou.basic.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    const val Y_M_D = "yyyy-MM-dd"
    const val Y_M_D_1 = "yyyyMMdd"
    const val Y_M_D_H_M_S = "yyyy-MM-dd hh:mm:ss"
    const val Y_M_D_H_M_S_S = "yyyy-MM-dd hh:mm:ss SSS"

    fun format(time: String, format: String): String {
        var date: Date? = null
        val formatDate = SimpleDateFormat(format, Locale.getDefault())
        try {
            date = DateFormat.getInstance().parse(time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date?.let { formatDate.format(it) } ?: ""
    }

    fun format(time: Long, format: String): String {
        val date = Date(time)
        val formatDate = SimpleDateFormat(format, Locale.getDefault())
        return formatDate.format(date)
    }
}