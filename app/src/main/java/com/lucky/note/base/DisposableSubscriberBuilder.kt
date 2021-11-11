package com.lucky.note.base

import io.reactivex.rxjava3.subscribers.DisposableSubscriber

private typealias OnCompleteCallback = () -> Unit
private typealias OnNextCallback<T> = (t: T) -> Unit
private typealias OnErrorCallback = (t: Throwable?) -> Unit

class DisposableSubscriberBuilder<T> : DisposableSubscriber<T>() {

    private var onCompleteCallback: OnCompleteCallback? = null
    private var onNextCallback: OnNextCallback<T>? = null
    private var onErrorCallback: OnErrorCallback? = null

    override fun onComplete() {
        onCompleteCallback?.invoke()
    }

    override fun onNext(t: T) {
        onNextCallback?.invoke(t)
    }

    override fun onError(t: Throwable?) {
        onErrorCallback?.invoke(t)
    }

    fun onComplete(callback: OnCompleteCallback) {
        onCompleteCallback = callback
    }

    fun onNext(callback: OnNextCallback<T>) {
        onNextCallback = callback
    }

    fun onError(callback: OnErrorCallback) {
        onErrorCallback = callback
    }

}

fun <T>registerDisposableSubscriber(function: DisposableSubscriberBuilder<T>.() -> Unit)
        = DisposableSubscriberBuilder<T>().also(function)