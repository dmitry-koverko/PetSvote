package com.petsvote.ui.dialogs

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import com.petsvote.ui.R
import com.petsvote.ui.databinding.DialogDeletePhotoBinding

class DeletePhotoDialogFragment(
    private val position: Int
    ): BaseDialog(R.layout.dialog_delete_photo, true) {

    private var mDeletePhotoDialogFragmentListener: DeletePhotoDialogFragmentListener? = null
    private lateinit var binding: DialogDeletePhotoBinding
    val TAG: String = DeletePhotoDialogFragment::class.java.name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogDeletePhotoBinding.bind(view)

        binding.deletePhoto.setOnClickListener {
            mDeletePhotoDialogFragmentListener?.delete(position)
            dismiss()
        }

        binding.cancel.setOnClickListener {
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

    fun setDeletePhotoDialogFragmentListener(listener: DeletePhotoDialogFragmentListener) {
        mDeletePhotoDialogFragmentListener = listener
    }

    interface DeletePhotoDialogFragmentListener {
        fun delete(position: Int)
    }
}