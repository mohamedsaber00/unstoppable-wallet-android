package io.horizontalsystems.bankwallet.core.web

import android.graphics.Bitmap
import android.os.Message
import androidx.compose.runtime.MutableState

interface WebDataListener {
    val progressState: MutableState<Float>
    val titleState: MutableState<String>
    val previewState: MutableState<Bitmap?>

    fun onCreateWindow(message: Message)
    fun onCloseWindow()
    suspend fun updateInfo()
    fun doUpdateVisitedHistory(url: String, isReload: Boolean)
    fun onReceivedIcon(icon: Bitmap?)
}