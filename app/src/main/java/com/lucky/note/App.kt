package com.lucky.note

import android.app.Application
import com.lucky.note.util.DeviceInfoUtils

/**
 * @Created by Walter on 2021/10/18
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        DeviceInfoUtils.init(this)
    }

    companion object {
        var instance: App? = null
    }

}