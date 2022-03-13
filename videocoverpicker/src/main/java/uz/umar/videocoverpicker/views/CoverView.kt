package uz.umar.videocoverpicker.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import uz.umar.videocoverpicker.R

class CoverView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    init {
        scaleType = ScaleType.CENTER_CROP
        alpha = 0.4f
        val width = resources.getDimensionPixelSize(R.dimen.video_width)
        val height = resources.getDimensionPixelSize(R.dimen.video_height)
        layoutParams = LinearLayout.LayoutParams(width, height)
    }
}