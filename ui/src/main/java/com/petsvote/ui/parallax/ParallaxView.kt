package com.petsvote.ui.parallax

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.petsvote.ui.R
import kotlin.math.abs
import kotlin.math.roundToInt

class ParallaxView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    init{
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.parallax_view, this, true)

        var viewPagerAdapter = ViewPagerAdapter(context, listOf<String>("dasd", "dadas", "dadasd"))

        findViewById<ViewPager2>(R.id.view_pager).apply {
            orientation = ORIENTATION_VERTICAL
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            })
            setPageTransformer { page, position ->
                //Log.d("ParallaxView", "${System.nanoTime()}position = $position")
                //Log.d("ParallaxView", "height = ${(position * (width / 2f)).roundToInt()    }")
                var opacity = Math.abs(Math.abs(position) - 1);
                page.alpha = opacity;

                if (position <= 1 && position >= -1) {


//                    lp.height = abs((position * (height / 2f)).roundToInt())
//                    page.findViewById<ImageView>(R.id.image).alpha = opacity
//                    if(position != 0.0f) {
//                        //page.findViewById<ImageView>(R.id.image).layoutParams = lp
//                    }
                }

                if(position >= -1 && position < 0){
//                    Log.d("PARALLAX", "heightView = " +
//                            "${page.findViewById<ImageView>(R.id.image).layoutParams.height}")
//                    Log.d("PARALLAX", "translation = " +
//                            "${page.findViewById<ImageView>(R.id.image).translationY}")
//
//                    //var t = abs( 1 - abs(position)) * height * 0.1f
//                    //var w = abs( 1 - abs(position)) * width
//                    var w = abs( position - 1) * width
//                    var h = abs( position - 1) * height
//                    //Log.d("PARALLAX", "heightView = $h")
//                    //Log.d("PARALLAX", "translation = $t")
//                    var lp = page.findViewById<ImageView>(R.id.image).layoutParams
//                    lp.height = (h.roundToInt() * 1).toInt()
//                    lp.width = w.roundToInt()
//                    page.findViewById<ImageView>(R.id.image).layoutParams = lp
                    //page.findViewById<ImageView>(R.id.image).translationY = - t


                    var h = abs( position - 1) * height
                    //var h = abs( 1 - abs(position)) * height
                    //Log.d("PARALLAX", "heightView = $h")
                    var lp = page.findViewById<ImageView>(R.id.image).layoutParams
                    lp.height = lp.height - h.roundToInt()
                    //page.findViewById<ImageView>(R.id.image).layoutParams = lp
                }

                if(position <= 1 && position > 0){
                    //var h = abs( position - 1) * height
                    var h = abs( 1 - abs(position)) * height
                    //Log.d("PARALLAX", "heightView = $h")
                    var lp = page.findViewById<ImageView>(R.id.image).layoutParams
                    lp.height = h.roundToInt()
                    //page.findViewById<ImageView>(R.id.image).layoutParams = lp
                }
            }
        }


    }

}