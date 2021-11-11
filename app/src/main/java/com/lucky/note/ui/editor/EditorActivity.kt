package com.lucky.note.ui.editor

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import com.lucky.note.R
import com.lucky.note.base.BaseActivity
import com.lucky.note.ui.widget.RichEditor
import com.lucky.note.ui.widget.Toolbar
import com.lucky.note.util.KeyBoardUtils
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.layout_format_bar.*

/**
 * @Created by Walter on 2021/10/19
 */
class EditorActivity : BaseActivity(), View.OnClickListener {

    private var lastSelectedColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        initToolbar()
        initEditor()
        initView()
    }

    private fun initToolbar() {
        tool_bar.apply {
            setDisplayMode(Toolbar.DISPLAY_ACTION_TEXT_WITH_MENU_TEXT)
            setActionText(getString(R.string.cancel))
            setMenuText(getString(R.string.save))
            setMenuTextEnable(false)
            setActionTextClickCallback {
                finish()
            }
        }
    }

    private fun initView() {
        iv_insert_photo.setOnClickListener(this)
        iv_format_bold.setOnClickListener(this)
        iv_format_italic.setOnClickListener(this)
        iv_format_underlined.setOnClickListener(this)
        iv_format_color.setOnClickListener(this)
        iv_format_bulleted_list.setOnClickListener(this)
        iv_format_numbered_list.setOnClickListener(this)
        iv_redo.setOnClickListener(this)
        iv_undo.setOnClickListener(this)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_bottom_silent, R.anim.activity_pop_side_out)
    }

    private fun initEditor() {
        rich_editor.setEditorFontSize(18)
        rich_editor.setEditorFontColor(resources.getColor(R.color.textBlack))
        rich_editor.setEditorBackgroundColor(Color.WHITE)
        rich_editor.setPadding(15, 15, 15, 15)  // 这里直接填px就可以，css里做了适配！
        rich_editor.setPlaceholder(getString(R.string.note_content_hint))
        rich_editor.setTextChangeListener(object : RichEditor.OnTextChangeListener {
            override fun onTextChange(text: String?) {
                if (TextUtils.isEmpty(et_title.text.toString().trim())) {
                    tool_bar.setMenuTextEnable(false)
                    return
                }
                if (TextUtils.isEmpty(text)) {
                    tool_bar.setMenuTextEnable(false)
                    return
                } else {
                    tool_bar.setMenuTextEnable(!TextUtils.isEmpty(Html.fromHtml(text)))
                }
            }

        })
        rich_editor.setDecorationStateListener(object : RichEditor.OnDecorationStateListener {
            override fun onStateChangeListener(text: String, types: List<RichEditor.Type>) {
                val flags = ArrayList<String>()
                for (type in types)
                    flags.add(type.name)
                iv_format_bold.isSelected = flags.contains("BOLD")
                iv_format_underlined.isSelected = flags.contains("UNDERLINE")
                iv_format_italic.isSelected = flags.contains("ITALIC")
                if (flags.contains("ORDEREDLIST")) {
                    iv_format_bulleted_list.isSelected = false
                    iv_format_numbered_list.isSelected = true
                } else {
                    iv_format_numbered_list.isSelected = false
                }
                if (flags.contains("UNORDEREDLIST")) {
                    iv_format_numbered_list.isSelected = false
                    iv_format_bulleted_list.isSelected = true
                } else {
                    iv_format_bulleted_list.isSelected = false
                }
            }

        })
        rich_editor.setImageClickListener(object : RichEditor.ImageClickListener {
            override fun onImageClick(imgUrl: String) {

            }

        })
    }

    private fun againEdit() {
        rich_editor.focusEditor()
        KeyBoardUtils.openKeyBoard(et_title, this)
    }

    private fun showColorPaletteDialog() {
        var fragment = supportFragmentManager.findFragmentByTag(ColorPaletteDialog.TAG)
        if (fragment == null)
            fragment = ColorPaletteDialog.newInstance(lastSelectedColor)
        val dialog = fragment as ColorPaletteDialog
        dialog.setColorChooseCallback {

        }
        dialog.show(supportFragmentManager, ColorPaletteDialog.TAG)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_insert_photo -> {

            }
            R.id.iv_format_bold -> {
                againEdit()
                rich_editor.setBold()
            }
            R.id.iv_format_italic -> {
                againEdit()
                rich_editor.setItalic()
            }
            R.id.iv_format_underlined -> {
                againEdit()
                rich_editor.setUnderline()
            }
            R.id.iv_format_color -> {
                KeyBoardUtils.closeKeyBoard(et_title, this)
                showColorPaletteDialog()
            }
            R.id.iv_format_bulleted_list -> {
                againEdit()
                rich_editor.setBullets()
            }
            R.id.iv_format_numbered_list -> {
                againEdit()
                rich_editor.setNumbers()
            }
            R.id.iv_redo -> {
                // 反撤销输入
                rich_editor.redo()
            }
            R.id.iv_undo -> {
                // 撤销输入
                rich_editor.undo()
            }
        }
    }

    companion object {

        @JvmStatic
        fun getCallingIntent(context: Context) = Intent(context, EditorActivity::class.java)

    }

}