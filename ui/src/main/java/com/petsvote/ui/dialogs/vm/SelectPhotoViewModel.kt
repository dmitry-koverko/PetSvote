package com.petsvote.ui.dialogs.vm

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.petsvote.ui.entity.LocalPhoto
import com.petsvote.ui.uriToBitmap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.coroutineContext

class SelectPhotoViewModel: ViewModel() {

    var localPhotos = MutableStateFlow<List<LocalPhoto>>(emptyList())

    suspend fun getLocalImages(cursor: Cursor?, contentResolver: ContentResolver) {
        var listPhoto = mutableListOf<LocalPhoto>()
        coroutineScope {
            cursor?.use {
                    cursor ->
                while (cursor.moveToNext()) {
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                    val id = cursor.getLong(idColumn)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    listPhoto.add(LocalPhoto(contentUri, null))
                }
            }
            localPhotos.value = listPhoto

        }
    }

}