package com.lucky.note.ui.editor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.lucky.note.R
import com.lucky.note.base.BaseDialogFragment
import com.lucky.note.util.DeviceInfoUtils
import kotlinx.android.synthetic.main.fragment_color_palette_dialog.*

/**
 * @Created by Walter on 2021/11/2
 */
class ColorPaletteDialog : BaseDialogFragment() {


    override fun onStart() {
        val width = DeviceInfoUtils.getScreenWidth()
        val height = (DeviceInfoUtils.getScreenHeight() * 0.75f).toInt()
        dialog?.window?.setLayout(width, height)
        val params = dialog?.window?.attributes
        params?.gravity = Gravity.BOTTOM
        activity?.apply {
            val color = ContextCompat.getColor(this, android.R.color.transparent);
            dialog?.window?.setBackgroundDrawable(ColorDrawable(color))
        }
        params?.windowAnimations = R.style.BottomDialogStyle
        dialog?.window?.attributes = params
        super.onStart()
    }


    override fun getLayoutId() = R.layout.fragment_color_palette_dialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        init()
    }

    private fun init() {
        color_palette.setColorChangeCallback {
            Log.i(TAG, "colorChangeCallback [0] = ${it[0]}, [1] = ${it[1]}, [2] = ${it[2]}")
            v_color_display.setBackgroundColor(Color.HSVToColor(it))
        }
        v_close_button.setOnClickListener { dismiss() }
        brightness_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                color_palette.setBrightness(progress / 100.0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }


    companion object {
        const val TAG = "ColorPaletteDialog"
        @JvmStatic
        fun newInstance() = ColorPaletteDialog()
    }


}