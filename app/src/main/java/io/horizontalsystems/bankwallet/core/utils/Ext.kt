package io.horizontalsystems.bankwallet.core.utils

import android.util.Log
import android.webkit.URLUtil
import io.horizontalsystems.bankwallet.core.web.util.AUTOLINK_WEB_URL
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import java.util.Locale

val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
    Timber.e("$context execute error, thread: ${Thread.currentThread().name}", throwable)
}

val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)
val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)


fun logD(vararg message: Any?) {
    Log.d("UnStoppable wallet", message.contentDeepToString())
}

fun logE(vararg message: Any?, throwable: Throwable = Throwable()) {
    Log.e("UnStoppable wallet", message.contentDeepToString(), throwable)
}

fun String?.formatToTwoDecimalPlaces(): String? {

    if (this.isNullOrBlank()) {
        return null
    }

    val volume = this.split(" ")[0].toDouble()
    val currency = this.split(" ")[1]
    return if (this.split(" ")[0].toDouble() < 0.01) {
        "<0.01 $currency"
    } else {
        String.format("%.2f $currency", volume)
    }
}

fun Double.toReadableString(): String {
    return when {
        this >= 1_000_000_000_000_000 -> String.format(Locale.US, "%.2fQ", this / 1_000_000_000_000_000)
        this >= 1_000_000_000_000 -> String.format(Locale.US, "%.2fT", this / 1_000_000_000_000)
        this >= 1_000_000_000 -> String.format(Locale.US, "%.2fB", this / 1_000_000_000)
        this >= 1_000_000 -> String.format(Locale.US, "%.2fM", this / 1_000_000)
        this >= 1_000 -> String.format(Locale.US, "%.2fK", this / 1_000)
        else -> String.format(Locale.US, "%.2f", this)
    }
}


fun Double.withSign(): String {
    val formattedNumber = this.toReadableString()
    return when {
        this < 0 -> formattedNumber
        else -> "+$formattedNumber"
    }
}

