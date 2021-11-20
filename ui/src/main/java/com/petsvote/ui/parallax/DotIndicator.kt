package com.petsvote.ui.parallax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.petsvote.ui.R

class DotIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = DotIndicator::class.simpleName

    private var widthView: Int = 0;
    private var heightView: Int = 0;
    private var canvas: Canvas? = null

    private var path = Path()

    private var radius = 0f

    var paint =
        Paint().apply {
            isAntiAlias = true
            color = Color.argb(50, 255, 255, 255)
            style = Paint.Style.FILL
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.DotIndicator){
            paint.color =
                getColor(R.styleable.DotIndicator_dot_background,
                ContextCompat.getColor(context, android.R.color.white))
        }
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        path.moveTo(radius, 0f)
        path.quadTo(radius /16, radius /16, 0f, radius)
        path.lineTo(0f, heightView - radius)
        path.quadTo((widthView /32).toFloat(), heightView -  (widthView /32).toFloat(),
            radius, heightView.toFloat())
        path.lineTo(widthView - radius, heightView.toFloat());
        path.quadTo(widthView - radius/16, heightView - radius/16,
                    widthView.toFloat(), heightView - radius)
        path.lineTo(widthView.toFloat(), radius)
        path.quadTo(widthView - radius/16, radius/16,
            widthView - radius, 0f);
        path.lineTo(radius, 0f)

        canvas.clipPath(path)
        canvas.drawPath(path, paint);
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
            else -> kotlin.math.min(requestedHeight, desiredHeight)
        }

        widthView = width;
        heightView = height;
        radius = kotlin.math.min(widthView, heightView).toFloat() /2

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val margins: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams::class.java.cast(layoutParams)
        val margin: Int = (7 * context.resources.displayMetrics.density).toInt()
        margins.bottomMargin = margin
        layoutParams = margins
    }
}