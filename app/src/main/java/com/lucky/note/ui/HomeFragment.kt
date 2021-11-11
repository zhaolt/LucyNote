package com.lucky.note.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.lucky.note.R
import com.lucky.note.base.BaseFragment
import com.lucky.note.base.registerDisposableSubscriber
import com.lucky.note.data.NoteDataSource
import com.lucky.note.data.NoteLocalSource
import com.lucky.note.data.NoteRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

/**
 * @Created by Walter on 2021/11/4
 */
class HomeFragment : BaseFragment() {

    private val noteDataSource: NoteDataSource = NoteRepository.getInstance(NoteLocalSource.instance)

    override fun getLayoutResId() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            noteDataSource.getAllNotes(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                    registerDisposableSubscriber {
                        onNext {
                            Log.i(TAG, "onNext: ${it.toString()}")
                        }

                        onError {
                            it?.printStackTrace()
                            Log.i(TAG, "onError")
                        }
                    }
                )
        }

    }



    companion object {
        private const val TAG = "HomeFragment"
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

}