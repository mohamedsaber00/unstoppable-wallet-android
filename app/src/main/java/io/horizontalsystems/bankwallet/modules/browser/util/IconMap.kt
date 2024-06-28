package io.horizontalsystems.bankwallet.modules.browser.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.Base64
import android.util.LruCache
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.mutableStateMapOf
import io.horizontalsystems.bankwallet.core.utils.ioScope
import io.horizontalsystems.bankwallet.core.utils.logD
import io.horizontalsystems.bankwallet.core.web.util.host
import io.horizontalsystems.core.CoreApp
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

object IconMap {

    private val iconDir = File(CoreApp.instance.filesDir, "icons").apply {
        if (!exists()) {
            mkdir()
        }
    }

    private val diskCache = object : LruCache<String, Bitmap?>(99) {
        override fun entryRemoved(
            evicted: Boolean,
            key: String,
            oldValue: Bitmap?,
            newValue: Bitmap?
        ) {
            if (newValue == null) {
                File(iconDir, generateName(key)).delete()
            }
        }

        override fun create(key: String): Bitmap? {
            val name = generateName(key)
            return try {
                BitmapFactory.decodeFile(File(iconDir, name).path)
            } catch (e: Exception) {
                null
            }
        }

        override fun sizeOf(key: String, value: Bitmap?): Int {
            return 1
        }
    }

    private fun generateName(key: String): String {
        return Base64.encodeToString(key.toByteArray(), Base64.URL_SAFE).trim()
    }

    private fun fetch(context: Context, key: String, scheme: String) {
        var iconReceived = false
        val webView = WebView(context)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                logD("onReceivedIcon $icon")
                if (!iconReceived) {
                    icon?.let {
                        iconReceived = true
                        save(key, it)
                    }
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                webView.loadUrl(request.url.toString())
                return true
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (error.errorCode == ERROR_CONNECT && scheme == "https") {
                        fetch(context, key, "http")
                    }
                }
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
                if (scheme == "https") {
                    fetch(context, key, "http")
                }
            }
        }
        webView.loadUrl("$scheme://$key/favicon.ico")
    }

    private val memoryCache = mutableStateMapOf<String, Bitmap>()

    operator fun get(url: String): Bitmap? {
        val key = url.host ?: ""
        if (key.isBlank()) return null
        val cache = memoryCache[key] ?: diskCache[key]?.apply { memoryCache[key] = this }
        if (cache == null) {
            fetch(CoreApp.instance, key, Uri.parse(url).scheme ?: "https")
        }
        return cache
    }

    private fun save(key: String, bitmap: Bitmap) {
        ioScope.launch {
            memoryCache[key] = bitmap
            val name = generateName(key)
            runCatching {
                FileOutputStream(File(iconDir, name)).use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, it)
                }
            }
        }
    }
}