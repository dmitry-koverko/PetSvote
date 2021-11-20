package com.petsvote.ui.parallax

import android.content.Context
import android.util.AttributeSet
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

    private var typeOrientation = 0
    private var dotCount = 0
    private var indicatorSize = 12
    private var currentPoint = 0

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
        if(dotCount != 0) setCountIndicators(dotCount)
    }

    fun setCountIndicators(count: Int){
        for(i in 1..count){
            var dot = DotIndicator(context)
            var side = (indicatorSize * context.resources.displayMetrics.density).toInt()
            var lp = ViewGroup.MarginLayoutParams(side, side)
            dot.apply {
                layoutParams = lp
                paint.color = ContextCompat.getColor(context, R.color.dot_white)
            }
            findViewById<LinearLayoutCompat>(R.id.root).addView(dot)
        }

        var lpLinear = LinearLayoutCompat.LayoutParams(
            (indicatorSize * context.resources.displayMetrics.density).toInt(),
            (((count * indicatorSize) + (count -1) * 7)
                    * context.resources.displayMetrics.density).toInt()
        )
        findViewById<LinearLayoutCompat>(R.id.root).layoutParams = lpLinear
    }

    fun setOffsetTo(percentOffset: Int){
        if(typeOrientation == 0){
            if(currentPoint == dotCount) return

        }
    }
   
}