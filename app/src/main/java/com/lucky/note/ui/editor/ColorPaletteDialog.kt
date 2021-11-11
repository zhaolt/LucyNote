package com.lucky.note.ui.editor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.lucky.note.R
import com.lucky.note.base.BaseDialogFragment
import com.lucky.note.data.ColorBoardItem
import com.lucky.note.ui.editor.adapter.ColorPaletteAdapter
import com.lucky.note.ui.editor.adapter.ColorPaletteItemDecoration
import com.lucky.note.util.DeviceInfoUtils
import kotlinx.android.synthetic.main.fragment_color_palette_dialog.*

/**
 * @Created by Walter on 2021/11/2
 */
class ColorPaletteDialog : BaseDialogFragment() {

    private var onColorChooseCallback: ((color: Int) -> Unit)? = null
    private val colorListAdapter = ColorPaletteAdapter()
    private var selectedColor: Int = 0

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
        arguments?.apply {
            selectedColor = getInt(EXTRA_CURRENT_COLOR)
            updateDisplayView(selectedColor)
        }
        color_palette.setColorChangeCallback {
            updateDisplayView(Color.HSVToColor(it))
        }
        v_close_button.setOnClickListener { dismiss() }
        v_choose_button.setOnClickListener {
            onColorChooseCallback?.invoke(selectedColor)
            dismiss()
        }
        brightness_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    color_palette.setBrightness(progress / 100.0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        val colorList = initPresetColor()
        colorListAdapter.addChildClickViewIds(R.id.root_frame)
        colorListAdapter.setOnItemChildClickListener { adapter, _, position ->
            val list = adapter.data as MutableList<ColorBoardItem>
            updateDisplayView(list[position].color)
            color_palette.update(list[position].color)
        }
        rv_color_fragment_list.layoutManager = GridLayoutManager(context, 6)
        rv_color_fragment_list.adapter = colorListAdapter
        rv_color_fragment_list.setHasFixedSize(true)
        rv_color_fragment_list.addItemDecoration(ColorPaletteItemDecoration())
        colorListAdapter.setNewInstance(colorList)
    }

    private fun initPresetColor(): MutableList<ColorBoardItem> {
        val colorList = ArrayList<ColorBoardItem>()
        colorList.add(ColorBoardItem(resources.getColor(R.color.textBlack), false))
        for (i in 0..10) {
            val hsv = FloatArray(3)
            hsv[0] = (i * 30).toFloat()
            hsv[1] = 1.0f
            hsv[2] = 1.0f
            colorList.add(ColorBoardItem(Color.HSVToColor(hsv), false))
        }
        return colorList
    }

    private fun updateDisplayView(color: Int) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        val seekProgress = (hsv[2] * 100f).toInt()
        brightness_seek_bar.progress = seekProgress
        selectedColor = color
        v_color_display.setBackgroundColor(color)
        val list = colorListAdapter.data
        for (c in list) {
            c.selected = false
            if (c.color == color)
                c.selected = true
        }
        colorListAdapter.notifyDataSetChanged()
    }

    fun setColorChooseCallback(callback: (color: Int) -> Unit) {
        onColorChooseCallback = callback
    }


    companion object {
        private const val EXTRA_CURRENT_COLOR = "CurrentColor"
        const val TAG = "ColorPaletteDialog"
        @JvmStatic
        fun newInstance(currentColor: Int) = ColorPaletteDialog().apply {
            val args = Bundle()
            args.putInt(EXTRA_CURRENT_COLOR, currentColor)
            arguments = args
        }
    }


}