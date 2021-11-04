package com.lucky.note.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * @Created by Walter on 2021/11/2
 */
abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    abstract fun getLayoutId(): Int

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        manager.executePendingTransactions()
    }

    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }
}