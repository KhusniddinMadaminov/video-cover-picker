package uz.umar.videocoverpicker.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt

object DisplayMetricsUtil {

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}