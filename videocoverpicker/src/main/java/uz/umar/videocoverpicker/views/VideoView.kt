package uz.umar.videocoverpicker.views

import android.content.Context
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView

class VideoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    TextureView(context, attrs), TextureView.SurfaceTextureListener {

    private var mediaPlayer: MediaPlayer? = null
    private var videoHeight = 0f
    private var videoWidth = 0f
    private var videoSizeDivisor = 1

    init {
        surfaceTextureListener = this
    }

    fun setDataSource(context: Context, uri: Uri, videoSizeDivisor: Int = 1) {
        this.videoSizeDivisor = videoSizeDivisor
        initPlayer()
        mediaPlayer?.setDataSource(context, uri)
        prepare()
    }

    fun seekTo(milliseconds: Int) {
        mediaPlayer?.seekTo(milliseconds)
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    private fun prepare() {
        mediaPlayer?.setOnVideoSizeChangedListener { _, width, height ->
            videoWidth = width.toFloat() / videoSizeDivisor
            videoHeight = height.toFloat() / videoSizeDivisor
            updateTextureViewSize()
            seekTo(0)
        }
        mediaPlayer?.prepareAsync()
    }

    private fun updateTextureViewSize() {
        var scaleX = 1.0f
        var scaleY = 1.0f

        if (videoWidth != videoHeight) {
            if (videoWidth > width && videoHeight > height) {
                scaleX = videoWidth / width
                scaleY = videoHeight / height
            } else if (videoWidth < width && videoHeight < height) {
                scaleY = width / videoWidth
                scaleX = height / videoHeight
            } else if (width > videoWidth) {
                scaleY = width / videoWidth / (height / videoHeight)
            } else if (height > videoHeight) {
                scaleX = height / videoHeight / (width / videoWidth)
            }
        }

        val matrix = Matrix().apply {
            setScale(scaleX, scaleY, (width / 2).toFloat(), (height / 2).toFloat())
        }

        setTransform(matrix)
    }

    private fun initPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
        mediaPlayer?.setSurface(Surface(surfaceTexture))
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean = false

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}
}