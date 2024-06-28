package io.horizontalsystems.bankwallet.core.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun View.removeFromParent() {
    val parent = parent as? ViewGroup
    parent?.removeView(this)
}


val View.activity: Activity?
    get() = context as? Activity


fun Modifier.tap(block: (Offset) -> Unit): Modifier {
    return pointerInput(Unit) {
        detectTapGestures(onTap = block)
    }
}