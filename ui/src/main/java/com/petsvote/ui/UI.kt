package com.petsvote.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.FileDescriptor
import java.io.IOException
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}

fun Shape.stateDisabled() {
    this.setBackgroundColor(ContextCompat.getColor(context, R.color.disable_btn))
    this.setTextColor(ContextCompat.getColor(context, R.color.disable_text_color))
}

fun Shape.stateEnabled() {
    this.setBackgroundColor(ContextCompat.getColor(context, R.color.ui_primary))
    this.setTextColor(ContextCompat.getColor(context, R.color.white))
}

fun ImageView.loadImage(url: String) {

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 50f
    //circularProgressDrawable.backgroundColor = ContextCompat.getColor(this.context, R.color.ui_gray)
    circularProgressDrawable.start()

    Glide
        .with(context)
        .load(url)
        .placeholder(circularProgressDrawable)
        //.transition(DrawableTransitionOptions.withCrossFade())
        //.skipMemoryCache(true)
        .into(this);
}

fun ImageView.loadImageSmall(url: String) {
    Glide
        .with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .skipMemoryCache(true)
        .into(this);
}

@MainThread
fun Fragment.uriToBitmapGlide(uri: Uri, view: CustomImageCropView) {
    Glide.with(this.requireContext()).asBitmap().load(uri).into(object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            var h: Int = resources.displayMetrics.heightPixels
            view.setImageBitmap(resource, h)
        }

        override fun onLoadCleared(placeholder: Drawable?) {
        }

    })
}

fun ImageView.loadImage(uri: Uri) {
    Glide
        .with(context)
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade())
        .skipMemoryCache(true)
        .into(this);
}

fun ImageView.loadImage(bitmap: Bitmap) {

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 50f
    //circularProgressDrawable.backgroundColor = ContextCompat.getColor(this.context, R.color.ui_gray)
    circularProgressDrawable.start()

    Glide
        .with(context)
        .load(bitmap)
        .placeholder(circularProgressDrawable)
        .transition(DrawableTransitionOptions.withCrossFade())
        .skipMemoryCache(true)
        .into(this);
}

fun Fragment.openUrl(url: String) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(url)
    startActivity(openURL)
}

fun Fragment.sendSupport() {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        val mailto = "mailto:petsvoteapp@gmail.com"
        data = Uri.parse(mailto)
        putExtra(Intent.EXTRA_SUBJECT, "")
        putExtra(Intent.EXTRA_TEXT, "")
    }
    startActivity(intent)
}

fun Fragment.ratingApp() {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(
            "https://play.google.com/store/apps/details?id=${this@ratingApp.context?.packageName}"
        )
        setPackage("com.android.vending")
    }
    startActivity(intent)
}

fun Fragment.shareApp() {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=${this@shareApp.context?.packageName}"
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.getMonthOnYear(value: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    var convertedDate = LocalDate.parse(value, formatter)
    var dateNow = LocalDate.now()
    val calendar: Calendar = GregorianCalendar.getInstance()
    val calendarOld: Calendar = GregorianCalendar.getInstance()
    calendarOld.set(Calendar.YEAR, -convertedDate.year)
    calendarOld.set(Calendar.MONTH, -convertedDate.month.value)
    calendarOld.set(Calendar.DAY_OF_MONTH, -convertedDate.dayOfMonth)

    val p: Period = Period.between(convertedDate, dateNow)
    var diff = p.years * 12 + p.months
    return if (diff < 12) "$diff ${this.getString(R.string.month1)}"
    else "${diff / 12}"
}

fun Context.uriToBitmap(selectedFileUri: Uri, scale: Float = 1f): Bitmap? {
    return uriToBitmap(selectedFileUri, scale, this.contentResolver)
}

fun uriToBitmap(
    selectedFileUri: Uri,
    scale: Float = 1f,
    contentResolver: ContentResolver
): Bitmap? {
    try {
        var parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        val exif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ExifInterface(fileDescriptor)
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        var matrix = getMatrix(orientation)
        val scaleWidth: Float = image.width / scale / image.width
        val scaleHeight: Float = image.height / scale / image.height
        matrix.preScale(scaleWidth, scaleHeight)
        parcelFileDescriptor.close()
        return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, false)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun uriToBitmapCamera(selectedFileUri: Uri): Bitmap? {
    return BitmapFactory.decodeFile(selectedFileUri.toString())
}

fun getMatrix(orientation: Int): Matrix {
    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
        else -> {
        }
    }
    return matrix
}

fun View.margin(
    left: Float? = null,
    top: Float? = null,
    right: Float? = null,
    bottom: Float? = null
) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

fun monthsBetweenDates(startDate: Date?, endDate: Date?): Int {
    val start = Calendar.getInstance()
    start.time = startDate
    val end = Calendar.getInstance()
    end.time = endDate
    var monthsBetween = 0
    var dateDiff = end[Calendar.DAY_OF_MONTH] - start[Calendar.DAY_OF_MONTH]
    if (dateDiff < 0) {
        val borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH)
        dateDiff = end[Calendar.DAY_OF_MONTH] + borrrow - start[Calendar.DAY_OF_MONTH]
        monthsBetween--
        if (dateDiff > 0) {
            monthsBetween++
        }
    } else {
        monthsBetween++
    }
    monthsBetween += end[Calendar.MONTH] - start[Calendar.MONTH]
    monthsBetween += (end[Calendar.YEAR] - start[Calendar.YEAR]) * 12
    return monthsBetween
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun dpToPx(dp: Float, context: Context): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

