package uz.umar.videocoverpicker.utils

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Encapsulates checking api levels.
 */
object ApiLevelHelper {

    @ChecksSdkIntAtLeast(parameter = 0)
    public fun isAtLeast(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT >= apiLevel
    }

    public fun isLowerThan(apiLevel: Int): Boolean {
        return Build.VERSION.SDK_INT < apiLevel
    }
}