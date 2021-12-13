package com.petsvote.ui.parallax

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
import androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import com.petsvote.ui.R

class PageIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val TAG = PageIndicator::class.java.name

    var typeOrientation = 0
    private var dotCount = 0
    private var indicatorSize = 0
    private var marginSize = 0
    var currentPoint = 0

    private var dotIndicator: DotIndicator

    init{
        context.withStyledAttributes(attrs, R.styleable.PageIndicator){
            typeOrientation = getInt(R.styleable.PageIndicator_page_orientation, 0)
            dotCount = getInt(R.styleable.PageIndicator_count_indicators, 0)
        }

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.page_indicator_view, this, true)

        findViewById<LinearLayoutCompat>(R.id.root).orientation =
            if(typeOrientation == 0) VERTICAL else HORIZONTAL


        dotIndicator = findViewById(R.id.dot_animation)

        indicatorSize = (12 * context.resources.displayMetrics.density).toInt()
        marginSize = (7 * context.resources.displayMetrics.density).toInt()

        if(dotCount != 0) setCountIndicators(dotCount)
    }

    fun setCountIndicators(count: Int){
        dotCount = count
        findViewById<LinearLayoutCompat>(R.id.root).removeAllViews()
        for(i in 1..count){
            var dot = DotIndicator(context)
            var lp = ViewGroup.MarginLayoutParams(indicatorSize, indicatorSize)
            if(typeOrientation == 1) lp.setMargins(marginSize, 0, 0 ,0)
            dot.apply {
                layoutParams = lp
                paint.color = ContextCompat.getColor(context, R.color.dot_white)
            }
            findViewById<LinearLayoutCompat>(R.id.root).addView(dot)
        }

        var lpLinearVertical = LinearLayoutCompat.LayoutParams(
            indicatorSize,
            (count * indicatorSize) + (count -1) * marginSize
        )

        var lpLinearHorizontal = LinearLayoutCompat.LayoutParams(
            (count * indicatorSize) + (count -1) * marginSize,
            indicatorSize
        )
        findViewById<LinearLayoutCompat>(R.id.root).layoutParams = if(typeOrientation == 0) lpLinearVertical else lpLinearHorizontal
    }

    fun setOffsetTo(percentOffset: Int){
        var distance = (indicatorSize + marginSize) * currentPoint
        val lp = dotIndicator.layoutParams
        if(typeOrientation == 0){
            if(currentPoint == dotCount) return
            if(percentOffset <= 50){
                var length = ((indicatorSize + marginSize) * percentOffset / 50) + indicatorSize
                lp.height = length
                dotIndicator.y = distance.toFloat()
            }else if(percentOffset > 50){
                var lengthOffset = ((indicatorSize + marginSize) * (percentOffset - 50) / 50)
                var offset = lengthOffset + distance
                var length = distance + ((indicatorSize * 2) + marginSize) - offset
                lp.height = length
                dotIndicator.y = offset.toFloat()

                Log.d(TAG, "lengthOffser = $lengthOffset " +
                        "offset = $offset length = $length")
            }
        }else {
            if(currentPoint == dotCount) return
            if(percentOffset <= 50){
                var width = ((indicatorSize + marginSize) * percentOffset / 50) + indicatorSize
                lp.width = width
                dotIndicator.x = distance.toFloat()
            }else {
                var lengthOffset = ((indicatorSize + marginSize) * (percentOffset - 50) / 50)
                var offset = lengthOffset + distance
                var width = distance + ((indicatorSize * 2) + marginSize) - offset
                lp.width = width
                dotIndicator.x = offset.toFloat()
            }
        }
        dotIndicator.layoutParams = lp
    }

}