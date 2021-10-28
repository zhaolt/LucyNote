package com.lucky.note.ui

import android.os.Bundle
import com.lucky.note.R
import com.lucky.note.base.BaseActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import java.util.concurrent.TimeUnit


/**
 * @Created by Walter on 2021/10/18
 */
class SplashActivity : BaseActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }


    override fun onResume() {
        super.onResume()
        val disposable = Observable.timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Long>() {
                override fun onComplete() {
                    startActivity(MainActivity.getCallingIntent(this@SplashActivity))
                    finish()
                }

                override fun onNext(t: Long?) {
                }

                override fun onError(e: Throwable?) {
                    e?.printStackTrace()
                }

            })
        disposables.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}