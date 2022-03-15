package uz.umar.videocoverpicker.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import uz.umar.videocoverpicker.R
import uz.umar.videocoverpicker.utils.DisplayMetricsUtil.dpToPx
import kotlin.math.ceil

object CoverUtils {

    private var standardCoverWidth = 56
    var totalHorizontalPadding = 32

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

    fun getCoverWidth(context: Context): Int {
        val screenWidth =
            context.resources.displayMetrics.run { widthPixels / density } - totalHorizontalPadding
        val approxThumbnailCount = ceil(((screenWidth) / standardCoverWidth).toDouble())
        var coverWidth = screenWidth / approxThumbnailCount
        coverWidth = dpToPx(context, coverWidth.toInt()).toDouble()
        return coverWidth.toInt()
    }

    fun getCoverHeight(context: Context): Int {
        return context.resources.getDimensionPixelSize(R.dimen.video_height)
    }

    fun getApproxThumbnailCount(context: Context): Int {
        val screenWidth =
            context.resources.displayMetrics.run { widthPixels / density } - totalHorizontalPadding
        return ceil(((screenWidth) / standardCoverWidth).toDouble()).toInt()
    }
}