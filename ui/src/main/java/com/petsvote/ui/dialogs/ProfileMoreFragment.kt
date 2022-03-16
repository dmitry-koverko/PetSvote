package com.petsvote.ui.dialogs

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogInformationSelectCountryBinding
import com.petsvote.ui.databinding.DialogProfileMoreBinding

class ProfileMoreFragment(): BaseDialog(R.layout.dialog_profile_more, true) {

    private var mProfileMoreFragmentListener: ProfileMoreFragmentListener? = null
    private lateinit var binding: DialogProfileMoreBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogProfileMoreBinding.bind(view)
        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.remove.setOnClickListener {
            mProfileMoreFragmentListener?.delete()
            dismiss()
        }

        binding.exit.setOnClickListener {

            mProfileMoreFragmentListener?.exit()
            dismiss()
        }

        startAnimtoTop()

    }

    private fun startAnimtoTop() {
        val propertyXLeft: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("TRANSITIONY", 500f, 0f)

        var animator = ValueAnimator()
        animator!!.setValues(propertyXLeft)
        animator!!.setDuration(200)
        animator!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            var pointY = animation.getAnimatedValue("TRANSITIONY") as Float
            binding.card.translationY = pointY
        })
        animator!!.start()
    }

    fun setProfileMoreFragmentListener(listener: ProfileMoreFragmentListener){
        mProfileMoreFragmentListener = listener
    }

    interface ProfileMoreFragmentListener{
        fun delete()
        fun exit()
    }
}