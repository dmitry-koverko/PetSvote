package com.petsvote.ui

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class BesieTransformation(private val context: Context): BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return besieCrop(pool, toTransform)
    }
    private fun besieCrop(pool: BitmapPool, source: Bitmap): Bitmap{
        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        var heightView = source.height
        var widthView = source.width

        val squared = Bitmap.createBitmap(source, x, y, size, size)

        var result: Bitmap? = pool[size, size, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = result?.let { Canvas(it) }
        val paint = Paint()
        paint.setShader(
            BitmapShader(
                squared,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
            )
        )
        paint.setAntiAlias(true)
        val radius = size / 2f
        var path = Path()
        path.reset()
        path.moveTo(radius, 0f)
        path.quadTo(radius /16, radius /16, 0f, radius)
        path.lineTo(0f, heightView - radius)
        path.quadTo((radius /16).toFloat(), heightView -  (radius /16).toFloat(),
            radius, heightView.toFloat())
        path.lineTo(widthView - radius, heightView.toFloat());
        path.quadTo(widthView - radius/16, heightView - radius/16,
            widthView.toFloat(), heightView - radius)
        path.lineTo(widthView.toFloat(), radius)
        path.quadTo(widthView - radius/16, radius/16,
            widthView - radius, 0f);
        path.lineTo(radius, 0f)

        canvas?.clipPath(path)
        canvas?.drawPath(path, paint);
        //canvas?.drawCircle(r, r, r, paint)
        return result!!
    }
}