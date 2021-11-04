package com.lucky.note.util

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

object DeviceInfoUtils {

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    fun init(context: Context) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y
    }

    fun getScreenWidth() = screenWidth

    fun getScreenHeight() = screenHeight

}