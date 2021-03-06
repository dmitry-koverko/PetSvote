package com.petsvote.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.alpha
import kotlin.math.roundToInt

class Star @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = Star::class.java.name

    private var typeStar = 0
    private var canvas: Canvas? = null

    private var widthView: Int = 0;
    private var heightView: Int = 0;

    private var iconBg = 0
    private var iconActive = 0

    private var starActiveSize = 0
    private var animator: ValueAnimator? = null
    private var isAmim = false
    private var isFind = false

    private var mOnClickListener: OnClickListener? = null

    init {
        iconActive = R.drawable.ic_star
        context.withStyledAttributes(attrs, R.styleable.Star){
            typeStar = getInt(R.styleable.Star_type_star, 0)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val requestedWidth = MeasureSpec.getSize(widthMeasureSpec)
        val requestedWidthMode = MeasureSpec.getMode(widthMeasureSpec)

        val requestedHeight = MeasureSpec.getSize(heightMeasureSpec)
        val requestedHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        val desiredWidth: Int = 0
        val desiredHeight: Int = 0

        val width = when (requestedWidthMode) {
            MeasureSpec.EXACTLY -> requestedWidth
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> Math.min(requestedWidth, desiredWidth)
        }

        val height = when (requestedHeightMode) {
            MeasureSpec.EXACTLY -> requestedHeight
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> Math.min(requestedHeight, desiredHeight)
        }

        widthView = width;
        heightView = height;

        setMeasuredDimension(width, height)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        try {
            this.canvas = canvas
            drawIcon()
            if(isAmim) drawIconActive()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }


    private fun drawIcon() {
        iconBg = when(typeStar){
            0 -> R.drawable.ic_star_1
            1 -> R.drawable.ic_star_2
            2 -> R.drawable.ic_star_3
            3 -> R.drawable.ic_star_4
            4 -> R.drawable.ic_star_5
            else -> R.drawable.ic_star_1
        }
        var bitmap = getVectorBitmap(iconBg)
        canvas!!.drawBitmap(
            bitmap,
            0f,
            0f, Paint()
        )
    }

    private fun drawIconActive() {
        var p = Paint()
        p.alpha = starActiveSize
        iconActive = when(typeStar){
            0 -> R.drawable.ic_star1
            1 -> R.drawable.ic_star2
            2 -> R.drawable.ic_star3
            3 -> R.drawable.ic_star4
            4 -> R.drawable.ic_star5
            else -> R.drawable.ic_star1
        }
        var bitmap = getVectorBitmap(iconActive)
        canvas!!.drawBitmap(
            bitmap,
            0f,
            0f, Paint()
        )
        if(starActiveSize >= 100){
            if(!isFind){
                isAmim = false
                invalidate()
            }
        }
    }

    private fun getVectorBitmap(iconResources: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, iconResources)
        val bitmap = Bitmap.createBitmap(
            widthView,
            heightView, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas)
        return bitmap
    }

    private fun getVectorBackground(iconResources: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, iconResources)
        val bitmap = Bitmap.createBitmap(
            starActiveSize,
            starActiveSize, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, starActiveSize, starActiveSize)
        drawable?.draw(canvas)
        return bitmap
    }

    fun animRipple(isFind: Boolean = false) {
        this.isFind = isFind
        isAmim = true
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_SIZE", 0f,100f)

        animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(500)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            starActiveSize = (animation.getAnimatedValue("PROPERTY_SIZE") as Float).toInt()
            invalidate()
        })
        animator!!.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //animRipple()
                mOnClickListener?.onClick(this)
            }
        }
        return true
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }
}