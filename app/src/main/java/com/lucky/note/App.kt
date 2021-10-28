package com.lucky.note

import android.app.Application

/**
 * @Created by Walter on 2021/10/18
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: App? = null
    }

}