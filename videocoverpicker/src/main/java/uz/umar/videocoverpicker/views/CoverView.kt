package uz.umar.videocoverpicker.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import uz.umar.videocoverpicker.utils.CoverUtils

class CoverView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    init {
        scaleType = ScaleType.CENTER_CROP
        alpha = 0.4f
        layoutParams = LinearLayout.LayoutParams(
            CoverUtils.getCoverWidth(context),
            CoverUtils.getCoverHeight(context)
        )
    }

}