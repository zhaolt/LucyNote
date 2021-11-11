package com.lucky.note.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.lucky.note.R
import kotlin.math.*


/**
 * @Created by Walter on 2021/10/29
 */
class ColorPalette : View {

    private var bigCircle: Int = 0
    private var rudeRadius: Int = 0
    private var handleColor: Int = 0
    private var handleStrokeWidth: Int = 5
    private var paint = Paint()
    private var handlePaint = Paint()
    private var centerPoint = Point()
    private var handlePosition = Point()
    private var colorChangeCallback: ((hsb: FloatArray) -> Unit)? = null
    private var handleDistance: Double = 0.0
    private var hue: Double = 0.0           // 色相
    private var brightness: Double = 1.0    // 亮度
    private var lastBrightness: Double = 1.0
    private var saturation: Double = 1.0    // 饱和度
    private var hsv = FloatArray(3)
    private lateinit var backgroundBitmap: Bitmap


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val types = context.obtainStyledAttributes(attrs, R.styleable.ColorPalette)
        try {
            bigCircle = types.getDimensionPixelOffset(
                R.styleable.ColorPalette_color_picker_circle_radius,
                100
            )
            rudeRadius = types.getDimensionPixelOffset(
                R.styleable.ColorPalette_color_picker_center_radius,
                10
            )
            handleColor = types.getColor(
                R.styleable.ColorPalette_color_picker_center_color,
                Color.WHITE
            )
        } finally {
            types.recycle()
        }
        centerPoint.set(bigCircle, bigCircle)
        handlePosition.set(centerPoint.x, centerPoint.y)
        paint.isAntiAlias = true
        backgroundBitmap = createColorWheelBitmap(bigCircle * 2, bigCircle * 2)
        handlePaint.isAntiAlias = true
        handlePaint.color = handleColor
        handlePaint.strokeWidth = handleStrokeWidth.toFloat()
        handlePaint.style = Paint.Style.STROKE
    }

    private fun createColorWheelBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val colorCount = 12
        val colorAngleStep = 360 / colorCount
        val colors = IntArray(colorCount + 1)
        val hsv = floatArrayOf(0f, 1f, brightness.toFloat())
        for (i in colors.indices) {
            hsv[0] = (360 - (i * colorAngleStep) % 360).toFloat()
            colors[i] = Color.HSVToColor(hsv)
        }
        colors[colorCount] = colors[0]
        val sweepGradient = SweepGradient(width / 2f, height / 2f, colors, null)
        val radialGradient = RadialGradient(
            width / 2f,
            height / 2f,
            bigCircle.toFloat(),
            0xFFFFFFFF.toInt(),
            0x00FFFFFF,
            Shader.TileMode.CLAMP
        )
        val composeShader = ComposeShader(sweepGradient, radialGradient, PorterDuff.Mode.SRC_OVER)
        paint.shader = composeShader
        val canvas = Canvas(bitmap)
        canvas.drawCircle(width / 2f, height / 2f, bigCircle.toFloat(), paint)
        return bitmap
    }

    private fun getDistance(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float
    ): Double {
        return sqrt(
            (x1 - x2.toDouble()).pow(2.0) + (y1 - y2.toDouble()).pow(2.0)
        )
    }

    private fun getHandlePoint(a: Point, b: Point, cutRadius: Int): Point {
        val radian = getRadian(a, b)
        return Point(
            a.x + (cutRadius * cos(radian.toDouble())).toInt(),
            a.x + (cutRadius * sin(radian.toDouble())).toInt()
        )
    }

    private fun getRadian(a: Point, b: Point): Float {
        val lenA = (b.x - a.x).toFloat()
        val lenB = (b.y - a.y).toFloat()
        val lenC = sqrt(lenA * lenA + lenB * lenB)
        var ang = acos(lenA / lenC)
        ang *= if (b.y < a.y) -1 else 1
        return ang
    }

    private fun getHue(centerX: Float, centerY: Float, rockX: Float, rockY: Float): Double {
        var hue = 0.0
        val deltaA = abs(rockX - centerX)
        val deltaB = abs(rockY - centerY)
        val deltaC = getDistance(centerX, centerY, rockX, rockY)
        if (centerX == rockX && centerY == rockY) return 0.0
        if (centerX == rockX) {
            hue = if (centerY > rockY) 90.0 else 270.0
            return hue
        }
        if (centerY == rockY) {
            hue = if (centerX > rockX) 180.0 else 0.0
            return hue
        }
        if (rockX > centerX && centerY > rockY)
            hue = asin(deltaB / deltaC) * 180 / PI
        else if (rockX < centerX && rockY < centerY)
            hue = asin(deltaA / deltaC) * 180 / PI + 90
        else if (rockX < centerX && rockY > centerY)
            hue = asin(deltaB / deltaC) * 180 / PI + 180
        else if (rockX > centerX && rockY > centerY)
            hue = asin(deltaA / deltaC) * 180 / PI + 270
        return hue
    }

    fun setColorChangeCallback(callback: (hsb: FloatArray) -> Unit) {
        colorChangeCallback = callback
    }

    fun setBrightness(brightness: Double) {
        if (brightness == lastBrightness)
            return
        this.brightness = brightness
        lastBrightness = brightness
        backgroundBitmap = createColorWheelBitmap(bigCircle * 2, bigCircle * 2)
        val h = hue.toFloat()
        val sat = saturation.toFloat()
        val brt = brightness.toFloat()
        hsv[0] = h
        hsv[1] = sat
        hsv[2] = brt
        colorChangeCallback?.invoke(hsv)
        invalidate()
    }

    fun update(color: Int) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        if (lastBrightness != hsv[2].toDouble()) {
            brightness = hsv[2].toDouble()
            lastBrightness = brightness
            backgroundBitmap = createColorWheelBitmap(bigCircle * 2, bigCircle * 2)
        }
        val radians = Math.toRadians(hsv[0].toDouble())
        Log.i(TAG, "update: hsv[1]: ${hsv[1]}")
        val cutRadius = bigCircle * hsv[1] - rudeRadius - 5
        val x = bigCircle + cutRadius * cos(radians)
        val y = bigCircle + cutRadius * sin(radians)
        handlePosition.x = x.toInt()
        handlePosition.y = y.toInt()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(bigCircle * 2, bigCircle * 2)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(backgroundBitmap, 0f, 0f, paint)
        canvas?.drawCircle(
            handlePosition.x.toFloat(),
            handlePosition.y.toFloat(),
            rudeRadius.toFloat(),
            handlePaint
        )
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                handleDistance = getDistance(
                    event.x,
                    event.y,
                    centerPoint.x.toFloat(),
                    centerPoint.y.toFloat()
                )
                if (handleDistance <= bigCircle - rudeRadius) {
                    handlePosition.set(event.x.toInt(), event.y.toInt())
                } else {
                    handlePosition = getHandlePoint(
                        centerPoint,
                        Point(event.x.toInt(), event.y.toInt()),
                        bigCircle - rudeRadius - 5
                    )
                }
                Log.i(TAG, "handlePosition: ${handlePosition.x}, ${handlePosition.y}")
                val cX = centerPoint.x.toFloat()
                val cY = centerPoint.y.toFloat()
                val pX = event.x
                val pY = event.y
                hue = getHue(cX, cY, pX, pY)
                if (hue < 0)
                    hue += 360.0
                val deltaX = abs(cX - pX).toDouble()
                val deltaY = (cY - cX).toDouble()
                saturation = (deltaX * deltaX + deltaY * deltaY).pow(0.5) / bigCircle * BASE_RADIO
                if (saturation <= 0) saturation = 0.0
                if (saturation >= 1.0) saturation = 1.0
            }
        }
        val h = hue.toFloat()
        val sat = saturation.toFloat()
        val brt = brightness.toFloat()
        hsv[0] = h
        hsv[1] = sat
        hsv[2] = brt
        Log.i(TAG, "hsv[0] = ${hsv[0]}, hsv[1] = ${hsv[1]}, hsv[2] = ${hsv[2]}")
        colorChangeCallback?.invoke(hsv)
        invalidate()
        return true
    }

    companion object {
        private const val TAG = "ColorPalette"
        private const val BASE_RADIO = 1.0f
    }

}