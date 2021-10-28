package com.lucky.note.ui.widget

import android.R
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import android.webkit.*
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import kotlin.math.abs

/**
 * @Created by Walter on 2021/10/20
 */
class RichEditor : WebView {

    enum class Type {
        BOLD,
        ITALIC,
        SUBSCRIPT,
        SUPERSCRIPT,
        STRIKETHROUGH,
        UNDERLINE,
        H1,
        H2,
        H3,
        H4,
        H5,
        H6,
        ORDEREDLIST,
        UNORDEREDLIST,
        JUSTIFYCENTER,
        JUSTIFYFULL,
        JUSTIFYLEFT,
        JUSTIFYRIGHT
    }

    protected var isReady: Boolean = false
    private var contents: String? = null
    private var mHeight = 0
    private var downX: Float = 0f
    private var downY: Float = 0f
    private var moveX: Float = 0f
    private var moveY: Float = 0f
    private var currentMS: Long = 0L
    private var imageClickListener: ImageClickListener? = null

    private var textChangeListener: OnTextChangeListener? = null
    private var decorationStateListener: OnDecorationStateListener? = null
    private var afterInitialLoadListener: AfterInitialLoadListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.webViewStyle)

    @SuppressLint("SetJavaScriptEnabled")
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        isVerticalScrollBarEnabled =false
        isHorizontalScrollBarEnabled = false
        settings.javaScriptEnabled = true
        webChromeClient = WebChromeClient()
        webViewClient = EditorWebViewClient()
        loadUrl(SETUP_URL)
        applyAttributes(context, attrs)
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?) {
        val attrsArray = intArrayOf(
            R.attr.gravity
        )
        val typeArray = context.obtainStyledAttributes(attrs, attrsArray)
        when (typeArray.getInt(0, NO_ID)) {
            Gravity.LEFT -> {
                execute("javascript:RE.setTextAlign(\"left\")")
            }
            Gravity.RIGHT -> {
                execute("javascript:RE.setTextAlign(\"right\")")
            }
            Gravity.TOP -> {
                execute("javascript:RE.setVerticalAlign(\"top\")")
            }
            Gravity.BOTTOM -> {
                execute("javascript:RE.setVerticalAlign(\"bottom\")")
            }
            Gravity.CENTER_HORIZONTAL -> {
                execute("javascript:RE.setTextAlign(\"center\")")
            }
            Gravity.CENTER_VERTICAL -> {
                execute("javascript:RE.setVerticalAlign(\"middle\")")
            }
            Gravity.CENTER -> {
                execute("javascript:RE.setVerticalAlign(\"middle\")")
                execute("javascript:RE.setTextAlign(\"center\")")
            }
        }
        typeArray.recycle()
    }

    fun setHtml(content: String?) {
        var html = ""
        if (content != null)
            html = content
        try {
            execute(
                "javascript:RE.setHtml('" + URLEncoder.encode(
                    html,
                    "UTF-8"
                ) + "');")
        } catch (e: UnsupportedEncodingException) {

        }
        contents = html
    }

    fun execute(trigger: String) {
        Log.i(TAG, "execute$trigger")
        if (isReady) {
            load(trigger)
        } else {
            postDelayed({
                load(trigger)
            }, 100)
        }
    }

    fun getHtml() = contents

    fun setTextChangeListener(listener: OnTextChangeListener) {
        textChangeListener = listener
    }

    fun setDecorationStateListener(listener: OnDecorationStateListener) {
        decorationStateListener = listener
    }

    fun setAfterInitialLoadListener(listener: AfterInitialLoadListener) {
        afterInitialLoadListener = listener
    }

    private fun load(trigger: String) {
        evaluateJavascript(trigger, null)
    }

    fun setEditorFontColor(color: Int) {
        val hex = convertHexColorString(color)
        execute("javascript:RE.setBaseTextColor('$hex');")
    }

    fun setEditorFontSize(px: Int) {
        execute("javascript:RE.setBaseFontSize('${px}px');")
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        execute("javascript:RE.setPadding('${left}px', '${top}px', '${right}px', '${bottom}px');")
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        setPadding(start, top, end, bottom)
    }

    private fun convertHexColorString(color: Int) = String.format("#%06X", (0xFFFFFF and color))

    private fun callback(text: String) {
        contents = text.replaceFirst(CALLBACK_SCHEME, "")
        textChangeListener?.onTextChange(contents)
    }

    private fun stateCheck(text: String) {
        val state = text.replaceFirst(STATE_SCHEME, "").toUpperCase(Locale.ENGLISH)
        val types = ArrayList<Type>()
        for (type in Type.values()) {
            if (TextUtils.indexOf(state, type.name) != -1)
                types.add(type)
        }
        decorationStateListener?.onStateChangeListener(state, types)
    }

    fun setEditorBackgroundColor(color: Int) {
        setBackgroundColor(color)
    }

    fun setPlaceholder(placeholder: String) {
        execute("javascript:RE.setPlaceholder('$placeholder');")
    }

    fun setInputEnabled(inputEnabled: Boolean) {
        execute("javascript:RE.setInputEnabled($inputEnabled)")
    }

    fun undo() {
        execute("javascript:RE.undo();")
    }

    fun redo() {
        execute("javascript:RE.redo();")
    }

    fun setBold() {
        execute("javascript:RE.setBold();")
    }

    fun setItalic() {
        execute("javascript:RE.setItalic();")
    }

    fun setUnderline() {
        execute("javascript:RE.setUnderline();")
    }

    fun setBullets() {
        execute("javascript:RE.setBullets();")
    }

    fun setNumbers() {
        execute("javascript:RE.setNumbers();")
    }

    fun insertImage(url: String) {
        execute("javascript:RE.prepareInsert();")
        val testStr = "<img src=\"$url\" alt=\"dachshund\" width=\"100%\"><br></br>"
        execute("javascript:RE.insetHTML('$testStr');")
    }

    fun scrollToBottom() {
        val temp = computeVerticalScrollRange()
        val valueAnimator = ValueAnimator.ofInt(mHeight, temp)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener { animation ->
            val nowHeight = animation.animatedValue as Int
            mHeight = nowHeight
            scrollTo(0, height)
            if (height == temp) {
                //再调用一次，解决不能滑倒底部
                scrollTo(0, computeVerticalScrollRange())
            }
        }
        valueAnimator.start()
    }

    fun focusEditor() {
        requestFocus()
        execute("javascript:RE.focus();")
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setImageClickListener(listener: ImageClickListener) {
        imageClickListener = listener
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    moveX = 0f
                    moveY = 0f
                    currentMS = System.currentTimeMillis()
                }
                MotionEvent.ACTION_MOVE -> {
                    moveX += abs(event.x - downX) //X轴距离
                    moveY += abs(event.y - downY) //y轴距离
                    downX = event.x
                    downY = event.y
                }
                MotionEvent.ACTION_UP -> {
                    var moveTime = System.currentTimeMillis() - currentMS
                    if (moveTime < 400 && (moveX < 25 && moveY < 25)) {
                        //这里是点击
                        val result = hitTestResult
                        if (null != result) {
                            val type = result.type
                            if (type == HitTestResult.IMAGE_TYPE) {
                                val imgUrl = result.extra
                                setInputEnabled(false)
                                postDelayed({
                                    imageClickListener?.apply {
                                        if (imgUrl!!.contains("file://")) {
                                            val url = imgUrl!!.replace("file://", "")
                                            onImageClick(url)
                                        } else {
                                            onImageClick(imgUrl)
                                        }
                                    }
                                }, 200L)
                            }
                        }
                    }
                }
            }
            return@setOnTouchListener false
        }
    }

    companion object {
        private const val TAG = "RichEditor"
        private const val SETUP_URL = "file:///android_asset/editor.html"
        private const val CALLBACK_SCHEME = "re-callback://"
        private const val STATE_SCHEME = "re-state://"
    }


    inner class EditorWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            isReady = url.equals(SETUP_URL, ignoreCase = true)
            afterInitialLoadListener?.onAfterInitialLoad(isReady)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            val decode = Uri.decode(url)
            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode)
                return true
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode)
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url.toString()
            val decode = Uri.decode(url)
            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode)
                return true
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode)
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    interface ImageClickListener {
        fun onImageClick(imgUrl: String)
    }

    interface OnTextChangeListener {
        fun onTextChange(text: String?)
    }

    interface OnDecorationStateListener {
        fun onStateChangeListener(text: String, types: List<Type>)
    }

    interface AfterInitialLoadListener {
        fun onAfterInitialLoad(isReady: Boolean)
    }

}