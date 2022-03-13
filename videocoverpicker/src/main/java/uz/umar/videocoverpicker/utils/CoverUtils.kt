package uz.umar.videocoverpicker.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build

object CoverUtils {

    fun getBitmapAtFrame(
        context: Context,
        uri: Uri,
        frameTime: Long,
        width: Int,
        height: Int
    ): Bitmap? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.Q)) {
            mediaMetadataRetriever.setDataSource(uri.path)
        } else {
            mediaMetadataRetriever.setDataSource(context, uri)
        }
        var bitmap = mediaMetadataRetriever.getFrameAtTime(
            frameTime,
            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
        )
        try {
            bitmap = bitmap?.let { Bitmap.createScaledBitmap(it, width, height, false) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}