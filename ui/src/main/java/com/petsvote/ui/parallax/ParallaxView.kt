package com.petsvote.ui.parallax

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.petsvote.ui.R
import kotlin.math.abs
import kotlin.math.roundToInt

class ParallaxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val TAG = ParallaxView::class.java.name
    private val pageIndicator: PageIndicator
    private var orientation: Int = 0

    init{
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.parallax_view, this, true)

        context.withStyledAttributes(attrs, R.styleable.ParallaxView){
            orientation = getInt(R.styleable.ParallaxView_pv_orientation, 0)
        }

        var viewPagerAdapter = ViewPagerAdapter(context,
            listOf<Int>(R.drawable.cat_card, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4))
        pageIndicator =  findViewById<PageIndicator>(R.id.page_indicators)
        pageIndicator.apply {
            typeOrientation = orientation
            setCountIndicators(viewPagerAdapter.itemCount)
        }

        var lp = pageIndicator.layoutParams as ConstraintLayout.LayoutParams
        if(orientation == 1) lp.apply {
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            bottomMargin = (12 * context.resources.displayMetrics.density).toInt()
            rightToRight = LayoutParams.PARENT_ID
            leftToLeft = LayoutParams.PARENT_ID
            topToTop = -1
        }

        findViewById<ViewPager2>(R.id.view_pager).apply {
            orientation = if(orientation == 0) ORIENTATION_VERTICAL else ORIENTATION_HORIZONTAL
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    Log.d(TAG, "OnPageChangeCallback " +
                            "positionOffset = ${positionOffset * 100} position = $position")
                    pageIndicator.apply {
                        currentPoint = position
                        if(positionOffset != 0.0f )
                            setOffsetTo((positionOffset * 100).toInt())
                    }
                }
            })
            setPageTransformer { page, position ->

            }
        }

    }

}