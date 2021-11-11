package com.lucky.note.ui.editor.adapter

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lucky.note.R
import com.lucky.note.data.ColorBoardItem
import com.lucky.note.util.DensityUtils
import com.lucky.note.util.DeviceInfoUtils

/**
 * @Created by Walter on 2021/11/10
 */
class ColorPaletteAdapter : BaseQuickAdapter<ColorBoardItem, BaseViewHolder>(R.layout.layout_color_palette_item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
        val size = ((DeviceInfoUtils.getScreenWidth() * 0.6f - DensityUtils.dp2px(context, 25f)) / 6f).toInt()
        val params = RecyclerView.LayoutParams(size, size)
        holder.itemView.layoutParams = params
        return holder
    }

    override fun convert(holder: BaseViewHolder, item: ColorBoardItem) {
        holder.getView<View>(R.id.v_color_board)
            .setBackgroundColor(item.color)
        holder.getView<View>(R.id.root_frame)
            .isSelected = item.selected
    }

}