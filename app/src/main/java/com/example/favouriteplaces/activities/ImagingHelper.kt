package com.example.favouriteplaces.activities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

class ImagingHelper {
    companion object {
        fun loadBitmap(context: Context, uri: Uri): Bitmap {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)

            inputStream.close()

            options.inSampleSize = 1
            options.inJustDecodeBounds = false

            val newInputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(newInputStream, null, options)
            newInputStream?.close()

            return bitmap ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }

    }
}
