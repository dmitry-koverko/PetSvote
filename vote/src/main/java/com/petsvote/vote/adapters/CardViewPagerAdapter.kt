package com.petsvote.vote.adapters

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.petsvote.api.entity.Pet
import com.petsvote.api.entity.PetRating
import com.petsvote.ui.layoutParams
import com.petsvote.ui.parallax.ParallaxView
import com.petsvote.vote.R

class CardViewPagerAdapter(
    private val context: Context,
    private var pet: Pet,
    private val height: Int) :
    RecyclerView.Adapter<CardViewPagerAdapter.MyViewHolder>() {

    private var scaleX = 1f
    private var scaleY = 1f
    private var scaleRate = 1f
    private var alphaBlack = 0f
    private var alphaText = 1f
    private var cafSize = 0.0f


    private var startAnim = false
    private var animator: ValueAnimator? = null

    fun submitList(petNew: Pet){
        this.pet = petNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var lp = holder.card.layoutParams

        var item = pet

        if(startAnim) {
            holder.card.scaleX = scaleX
            holder.card.scaleY = scaleY
            holder.view_black.visibility = View.VISIBLE
            holder.view_black.alpha = alphaBlack
            holder.title.alpha = alphaText
            holder.description.alpha = alphaText
            holder.rate.visibility = View.VISIBLE
            holder.rate.scaleX = scaleRate
            holder.rate.scaleY = scaleRate

        }else {
            lp.height = height
            holder.card.layoutParams = lp
            holder.card.scaleX = 1f
            holder.card.scaleY = 1f
            holder.view_black.visibility = View.GONE
            holder.view_black.alpha = 0f
            holder.title.alpha = 1f
            holder.description.alpha = 1f
            holder.rate.visibility = View.GONE
            holder.rate.scaleX = 1f
            holder.rate.scaleY = 1f
        }
        var listPhoto = mutableListOf<String>()
        item.photos?.let {
            for (i in it){
                listPhoto.add(i.url)
            }
        }
        holder.paralax.list = listPhoto
        holder.title.text = item.name
        holder.title.text = item.name
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun startAnim(){
        startAnim = true

        val propertyScaleY: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_SCALE_Y", 1f, 0.97f)
        val propertyScaleX: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_SCALE_X", 1f, 0.95f)
        val propertyScaleRate: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_SCALE_RATE", 1f, 0.8f)
        val propertyAlpha: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_ALPHA", 0f, 0.5f)
        val propertyAlphaText: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("PROPERTY_ALPHA_TEXT", 1f, 0f)

        animator = ValueAnimator()
        animator!!.setValues(propertyScaleY, propertyScaleX, propertyAlpha,
            propertyAlphaText, propertyScaleRate)
        animator!!.setDuration(400)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            scaleY = animation.getAnimatedValue("PROPERTY_SCALE_Y") as Float
            scaleX = animation.getAnimatedValue("PROPERTY_SCALE_X") as Float
            scaleRate = animation.getAnimatedValue("PROPERTY_SCALE_RATE") as Float
            alphaBlack = animation.getAnimatedValue("PROPERTY_ALPHA") as Float
            alphaText = animation.getAnimatedValue("PROPERTY_ALPHA_TEXT") as Float
            notifyDataSetChanged()
        })
        animator!!.addListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                startAnim = false
                notifyDataSetChanged()
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationRepeat(p0: Animator?) {}

        })
        animator!!.start()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card: CardView
        var view_black: View
        var title: TextView
        var description: TextView
        var rate: ImageView
        var paralax: ParallaxView

        init {
            card = itemView.findViewById(R.id.card)
            view_black = itemView.findViewById(R.id.view_black)
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            rate = itemView.findViewById(R.id.rate)
            paralax = itemView.findViewById(R.id.parallax)
        }
    }

}