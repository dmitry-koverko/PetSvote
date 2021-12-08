package com.petsvote.ui.crop


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import com.takusemba.cropme.CropOverlay

class CustomCropOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    cropOverlayAttrs: AttributeSet? = attrs
) : CropOverlay(context, attrs, defStyleAttr, cropOverlayAttrs) {

    override fun drawCrop(canvas: Canvas, paint: Paint) {
        val frameRect = frame ?: return
        val frameWidth = frameRect.width()
        val frameHeight = frameRect.height()

        var left = (width - frameWidth) / 2f
        var top = (height - frameHeight) / 2f
        var right = (width + frameWidth) / 2f
        var bottom = (height + frameHeight) / 2f

        left = 16 * context.resources.displayMetrics.density
        right = width - left

        var centerY = height / 2f
        var withFrame = (width - left * 2) / 2
        top = centerY - withFrame
        bottom = centerY + withFrame
        ROUND = withFrame

        if (VERSION_CODES.LOLLIPOP < SDK_INT) {
            canvas.drawRoundRect(left, top, right, bottom, ROUND, ROUND, paint)
        } else {
            canvas.drawRect(left, top, right, bottom, paint)
        }
    }

    override fun drawBorder(canvas: Canvas, paint: Paint) {

    }

    companion object {

        private var ROUND: Float = 25f
    }
}