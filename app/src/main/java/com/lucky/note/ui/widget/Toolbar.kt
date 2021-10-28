package com.lucky.note.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.lucky.note.R
import kotlinx.android.synthetic.main.layout_toolbar.view.*

/**
 * @Created by Walter on 2021/10/19
 */
class Toolbar : FrameLayout {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.layout_toolbar, this)
    }

    fun setDisplayMode(mode: Int) {
        iv_action.visibility =
            if ((mode and DISPLAY_NO_ACTION_BUTTON) != DISPLAY_NO_ACTION_BUTTON) View.VISIBLE
            else View.GONE
        tv_action.visibility =
            if ((mode and DISPLAY_NO_ACTION_TEXT) != DISPLAY_NO_ACTION_TEXT) View.VISIBLE
            else View.GONE
        tv_title.visibility =
            if ((mode and DISPLAY_NO_TITLE) != DISPLAY_NO_TITLE) View.VISIBLE
            else View.GONE
        tv_menu.visibility =
            if ((mode and DISPLAY_NO_MENU_TEXT) != DISPLAY_NO_MENU_TEXT) View.VISIBLE
            else View.GONE
        iv_menu.visibility =
            if ((mode and DISPLAY_NO_MENU_BUTTON) != DISPLAY_NO_MENU_BUTTON) View.VISIBLE
            else View.GONE
    }


    fun setActionTextClickCallback(callback: () -> Unit) {
        tv_action.setOnClickListener { callback() }
    }

    fun setActionButtonClickCallback(callback: () -> Unit) {
        iv_action.setOnClickListener { callback() }
    }

    fun setMenuTextClickCallback(callback: () -> Unit) {
        tv_menu.setOnClickListener { callback() }
    }

    fun setMenuButtonClickCallback(callback: () -> Unit) {
        iv_menu.setOnClickListener { callback() }
    }

    fun setActionButtonIcon(resId: Int) {
        iv_action.setImageResource(resId)
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    fun setActionText(content: String) {
        tv_action.text = content
    }

    fun setMenuButtonIcon(resId: Int) {
        iv_menu.setImageResource(resId)
    }

    fun setMenuText(content: String) {
        tv_menu.text = content
    }

    fun setMenuTextEnable(isEnable: Boolean) {
        tv_menu.isEnabled = isEnable
    }





    companion object {
        // 0000 0010
        private const val DISPLAY_NO_ACTION_BUTTON = 2
        // 0000 0100
        private const val DISPLAY_NO_ACTION_TEXT = 4
        // 0000 1000
        private const val DISPLAY_NO_TITLE = 8
        // 0001 0000
        private const val DISPLAY_NO_MENU_BUTTON = 16
        // 0010 0000
        private const val DISPLAY_NO_MENU_TEXT = 32

        const val DISPLAY_ACTION_TEXT_WITH_MENU_TEXT =
            DISPLAY_NO_ACTION_BUTTON or DISPLAY_NO_TITLE or DISPLAY_NO_MENU_BUTTON
    }

}