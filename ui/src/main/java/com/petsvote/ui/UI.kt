package com.petsvote.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.signature.ObjectKey

import com.bumptech.glide.request.RequestOptions

import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.FileDescriptor
import java.io.IOException


fun ImageView.loadImage(url: String){

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 50f
    //circularProgressDrawable.backgroundColor = ContextCompat.getColor(this.context, R.color.ui_gray)
    circularProgressDrawable.start()

    Glide
        .with(context)
        .load(url)
        .placeholder(circularProgressDrawable)
        .transition(DrawableTransitionOptions.withCrossFade())
        .skipMemoryCache(true)
        .into(this);
}

fun Context.uriToBitmap(selectedFileUri: Uri): Bitmap? {
    try {
        val parcelFileDescriptor = applicationContext?.contentResolver?.openFileDescriptor(selectedFileUri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun dpToPx(dp: Float, context: Context): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()