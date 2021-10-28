package com.lucky.note.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lucky.note.R
import com.lucky.note.base.BaseActivity
import com.lucky.note.ui.editor.EditorActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startEdit(v: View) {
        startActivity(EditorActivity.getCallingIntent(this))
    }

    companion object {
        @JvmStatic
        fun getCallingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

}