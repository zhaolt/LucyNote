package com.lucky.note.util

import android.content.Context

/**
 * @Created by Walter on 2021/10/28
 */
object DensityUtils {
    fun dp2px(context: Context, dp: Float) : Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun px2dp(context: Context, px: Float) : Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }
}