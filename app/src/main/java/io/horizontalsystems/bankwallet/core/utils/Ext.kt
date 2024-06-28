package io.horizontalsystems.bankwallet.core.utils

import android.util.Log
import android.webkit.URLUtil
import io.horizontalsystems.bankwallet.core.web.util.AUTOLINK_WEB_URL
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

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

