package uz.umar.videocoverpicker.views

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.timeline_view.view.*
import uz.umar.videocoverpicker.R
import uz.umar.videocoverpicker.listener.SeekListener
import uz.umar.videocoverpicker.utils.CoverUtils
import uz.umar.videocoverpicker.utils.DisplayMetricsUtil
import kotlin.math.roundToInt

class CoverTimeLine @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs) {

    private var frameDimension: Int = 0
    var currentProgress = 0.0
    var currentSeekPosition = 0f
    var seekListener: SeekListener? = null
    var uri: Uri? = null
        set(value) {
            field = value
            field?.let {
                loadThumbnails(it)
                invalidate()
                view_seek_bar.setDataSource(context, it, 4)
                view_seek_bar.seekTo(currentSeekPosition.toInt())
            }
        }

    init {
        View.inflate(getContext(), R.layout.timeline_view, this)
        frameDimension = context.resources.getDimensionPixelOffset(R.dimen.video_height)
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_MOVE -> handleTouchEvent(event)
        }
        return true
    }

    private fun handleTouchEvent(event: MotionEvent) {
        val seekViewWidth = context.resources.getDimensionPixelSize(R.dimen.seekbar_width)
        currentSeekPosition = (event.x.roundToInt() - (seekViewWidth / 2)).toFloat()

        val availableWidth =
            container_covers.width - DisplayMetricsUtil.dpToPx(
                context,
                2 * CoverUtils.totalHorizontalPadding
            )
        if (currentSeekPosition + seekViewWidth > container_covers.right) {
            currentSeekPosition = (container_covers.right - seekViewWidth).toFloat()
        } else if (currentSeekPosition < container_covers.left) {
            currentSeekPosition = paddingStart.toFloat()
        }

        currentProgress = (currentSeekPosition.toDouble() / availableWidth.toDouble()) * 100
        container_seek_bar.translationX = currentSeekPosition
        val seekbarPos = ((currentProgress * view_seek_bar.getDuration()) / 100).toInt()
        view_seek_bar.seekTo(seekbarPos)

        seekListener?.onVideoSeeked(currentProgress)
    }

    private fun loadThumbnails(uri: Uri) {
        val metaDataSource = MediaMetadataRetriever()
        metaDataSource.setDataSource(context, uri)

        val videoLength = ((metaDataSource.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )?.toInt() ?: 0) * 1000).toLong()

        val thumbnailCount = CoverUtils.getApproxThumbnailCount(context)

        val interval = videoLength / thumbnailCount

        for (i in 0 until thumbnailCount) {
            val frameTime = i * interval
            var bitmap =
                metaDataSource.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            try {
                val targetWidth: Int
                val targetHeight: Int
                if (bitmap != null) {
                    if (bitmap.height > bitmap.width) {
                        targetHeight = frameDimension
                        val percentage = frameDimension.toFloat() / bitmap.height
                        targetWidth = (bitmap.width * percentage).toInt()
                    } else {
                        targetWidth = frameDimension
                        val percentage = frameDimension.toFloat() / bitmap.width
                        targetHeight = (bitmap.height * percentage).toInt()
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            container_covers.addView(CoverView(context).apply { setImageBitmap(bitmap) })
        }
        metaDataSource.release()
    }
}