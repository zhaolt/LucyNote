package com.lucky.note.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lucky.note.R
import com.lucky.note.base.BaseActivity
import com.lucky.note.ui.editor.EditorActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        fab_add.setOnClickListener {
            startActivity(EditorActivity.getCallingIntent(this))
            overridePendingTransition(R.anim.activity_pop_side_in, R.anim.activity_bottom_silent)
        }
        loadFragment()
    }

    private fun initToolbar() {
        setSupportActionBar(tool_bar)
    }

    private fun loadFragment() {
        var fragment = supportFragmentManager.findFragmentById(R.id.root_frame)
        if (fragment == null)
            fragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.root_frame, fragment).commit()
    }

    companion object {
        @JvmStatic
        fun getCallingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

}