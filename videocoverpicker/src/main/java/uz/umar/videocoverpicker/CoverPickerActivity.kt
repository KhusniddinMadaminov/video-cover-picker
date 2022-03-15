package uz.umar.videocoverpicker

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_cover_picker.*
import uz.umar.videocoverpicker.listener.SeekListener

class CoverPickerActivity : AppCompatActivity() {

    companion object {
        const val COVER_POSITION = "COVER_POSITION"
        const val VIDEO_URI = "VIDEO_URI"
        fun getStartIntent(context: Context, uri: Uri, thumbnailPosition: Long = 0): Intent {
            val intent = Intent(context, CoverPickerActivity::class.java)
            intent.putExtra(VIDEO_URI, uri)
            intent.putExtra(COVER_POSITION, thumbnailPosition)
            return intent
        }
    }

    private lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover_picker)
        videoUri = intent.getParcelableExtra(VIDEO_URI)!!
        setupVideoContent()
        done_button.setOnClickListener { finishWithData() }
        cancel_button.setOnClickListener { finish() }
    }

    private fun setupVideoContent() {
        view_thumbnail.setDataSource(this, videoUri)
        thumbs.seekListener = seekListener
        thumbs.currentSeekPosition = intent.getLongExtra(COVER_POSITION, 0).toFloat()
        thumbs.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                thumbs.viewTreeObserver.removeOnGlobalLayoutListener(this)
                thumbs.uri = videoUri
            }
        })
    }

    private val seekListener = object : SeekListener {
        override fun onVideoSeeked(percentage: Double) {
            val duration = view_thumbnail.getDuration()
            view_thumbnail.seekTo((percentage.toInt() * duration) / 100)
        }
    }

    private fun finishWithData() {
        val intent = Intent()
        intent.putExtra(
            COVER_POSITION,
            ((view_thumbnail.getDuration() / 100) * thumbs.currentProgress).toLong() * 1000
        )
        intent.putExtra(VIDEO_URI, videoUri)
        setResult(RESULT_OK, intent)
        finish()
    }
}