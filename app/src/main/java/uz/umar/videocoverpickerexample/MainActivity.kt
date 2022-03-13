package uz.umar.videocoverpickerexample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import uz.umar.videocoverpicker.CoverPickerActivity
import uz.umar.videocoverpicker.CoverPickerActivity.Companion.COVER_POSITION
import uz.umar.videocoverpicker.CoverPickerActivity.Companion.VIDEO_URI
import uz.umar.videocoverpicker.utils.CoverUtils

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PICK_MEDIA = 1000
    private val REQUEST_CODE_PICK_COVER = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pick_cover.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Select video"
                ), REQUEST_CODE_PICK_MEDIA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_MEDIA) {
                data?.data?.let {
                    startActivityForResult(
                        CoverPickerActivity.getStartIntent(this, it),
                        REQUEST_CODE_PICK_COVER
                    )
                }
            } else if (requestCode == REQUEST_CODE_PICK_COVER) {
                val imageUri = data!!.getParcelableExtra<Uri>(VIDEO_URI)!! as Uri
                val location = data.getLongExtra(COVER_POSITION, 0)
                val bitmap = CoverUtils.getBitmapAtFrame(
                    this, imageUri, location,
                    160, 240
                )
                cover_image.setImageBitmap(bitmap)
            }
        }
    }
}