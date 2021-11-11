package com.lucky.note.ui.editor.adapter

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucky.note.util.DensityUtils

/**
 * @Created by Walter on 2021/11/10
 */
class ColorPaletteItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager as GridLayoutManager
        val position = parent.getChildAdapterPosition(view)
        val spanCount = layoutManager.spanCount
        val column = position % spanCount
        val spacing = DensityUtils.dp2px(view.context, 5f)
        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column - 1) * spacing / spanCount
        if (position >= spanCount) {
            outRect.top = spacing
        }
    }

}