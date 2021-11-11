package com.lucky.note.base

/**
 * @Created by Walter on 2021/11/8
 */
open class SingletonHolder<out T: Any, in A>(private val creator: (A) -> T) {
    private var instance: T? = null
    fun getInstance(arg: A): T = instance ?: synchronized(this) {
        instance ?: creator(arg).apply {
            instance = this
        }
    }
}