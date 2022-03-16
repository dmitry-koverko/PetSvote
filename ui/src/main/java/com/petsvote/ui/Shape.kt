package com.petsvote.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import android.animation.PropertyValuesHolder
import android.view.MotionEvent
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.os.Build
import android.text.Layout
import android.text.TextPaint
import android.text.StaticLayout
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import android.graphics.Paint
import android.widget.FrameLayout


open class Shape @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mOnClickListener: OnClickListener? = null

    private val TAG = Shape::class.simpleName

    private var widthView: Int = 0;
    private var heightView: Int = 0;
    private val customTypeface = ResourcesCompat.getFont(context, R.font.myfont)

    private var p: Paint? = null
    private var path: Path? = null
    private var canvas: Canvas? = null
    private var animator: ValueAnimator? = null

    var text: String = ""
        @RequiresApi(Build.VERSION_CODES.O)
        set(value) {
            field = value
            onDraw(canvas)
        }
    private var textSize = 15

    private var pRipple: Paint? = null
    private var pathRipple: Path? = null
    private var paintText: Paint? = null
    private var xTouch = 0f;
    private var yTouth = 0f;
    private var r : Rect = Rect()
    
    private var rippleRadius = 0f;
    
    private var isAmim = false

    init {

        p = Paint(Paint.ANTI_ALIAS_FLAG)
        p!!.strokeWidth = 30f
        p!!.setColor(Color.BLACK);
        p!!.setStyle(Paint.Style.FILL);
        path = Path()

        pRipple = Paint(Paint.ANTI_ALIAS_FLAG)
        pRipple!!.strokeWidth = 3f
        pRipple!!.setColor(Color.argb(50, 255, 255, 255));
        pRipple!!.setStyle(Paint.Style.FILL);
        pathRipple = Path()

        paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        paintText!!.setColor(Color.WHITE)
        paintText!!.strokeWidth = 5f
        paintText!!.typeface = Typeface.create(customTypeface, Typeface.NORMAL)

        context.withStyledAttributes(attrs, R.styleable.Shape) {
            p!!.setColor(getInt(R.styleable.Shape_besie_background, Color.BLACK))
            pRipple!!.setColor(getInt(R.styleable.Shape_besie_ripple, Color.DKGRAY))
            text = getString(R.styleable.Shape_besie_text) as String
            textSize = getInt(R.styleable.Shape_besie_text_size, 15)
            paintText!!.setColor(getInt(R.styleable.Shape_besie_text_color, Color.WHITE))
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
        try{
            this.canvas = canvas
            calculate()
            if(!text.isNullOrEmpty()) drawText()
        }catch (e: Exception){
            Log.d(TAG, e.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawText(){
        paintText!!.textSize = textSize * resources.displayMetrics.density
        canvas!!.getClipBounds(r)
        val cHeight: Int = r.height()
        val cWidth: Int = r.width()
        paintText!!.setTextAlign(Paint.Align.LEFT)
        paintText!!.getTextBounds(text, 0, text.length, r)
        val x: Float = cWidth / 2f - r.width() / 2f - r.left
        val y: Float = cHeight / 2f + r.height() / 2f - r.bottom
        canvas!!.drawText(text, x, y, paintText!!);

    }

    private fun calculate() {

        path!!.reset()
        path!!.moveTo((heightView /2).toFloat(), 0f);
        path!!.quadTo((heightView /32).toFloat(), (heightView /32).toFloat(), 0f, (heightView/2).toFloat());
        path!!.quadTo((heightView /32).toFloat(), heightView -  (heightView /32).toFloat(), (heightView / 2).toFloat(), heightView.toFloat());
        path!!.lineTo((widthView - heightView / 2).toFloat(), heightView.toFloat());
        path!!.quadTo((widthView - heightView / 32).toFloat(), (heightView - heightView /32).toFloat(), widthView.toFloat(), (heightView /2).toFloat());
        path!!.quadTo((widthView - heightView / 32).toFloat(), (heightView /32).toFloat(), (widthView - heightView /2).toFloat(), 0f);
        path!!.lineTo((heightView / 2).toFloat(), 0f);
        canvas!!.clipPath(path!!)
        canvas!!.drawPath(path!!, p!!);

        if(isAmim){
            drawRipple(canvas!!)
        }
    }

    private fun drawRipple(canvas: Canvas){
        canvas!!.drawCircle(xTouch, yTouth, rippleRadius, pRipple!!);
        if(rippleRadius >= widthView){
            isAmim = false
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        xTouch = event!!.x
        yTouth = event!!.y

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isAmim = true
                animRipple()
                mOnClickListener?.onClick(this)
            }
        }
        return true
    }

    fun animRipple(){
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_RADIUS", 0f, widthView.toFloat())

        animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(600)
        animator!!.addUpdateListener(AnimatorUpdateListener { animation ->
            rippleRadius = animation.getAnimatedValue("PROPERTY_RADIUS") as Float
            invalidate()
        })
        animator!!.start()
    }

    fun setRippleColor(color: Int){
        pRipple!!.setColor(color)
        invalidate()
    }
    override fun setBackgroundColor(color: Int){
        p!!.setColor(color)
        invalidate()
    }

    fun setTextColor(color: Int){
        paintText!!.setColor(color)
        invalidate()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClickListener = l
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}