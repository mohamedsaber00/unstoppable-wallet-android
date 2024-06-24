package io.horizontalsystems.bankwallet.core.web.util

import android.net.Uri
import android.webkit.URLUtil

fun String.toUrl(): String {
    if (isUrl()) {
        return URLUtil.guessUrl(this.trim())
    }
    return toSearchUrl()
}

fun String.isUrl(): Boolean {
    val inUrl = this.trim()
    if (inUrl.contains(" ")) {
        return false
    }
    if (URLUtil.isValidUrl(inUrl)) {
        return true
    }
    val matcher = AUTOLINK_WEB_URL.matcher(inUrl)
    if (matcher.matches()) {
        return true
    }
    return false
}

fun String.toSearchUrl(): String {
    return String.format(NameValue("Google", "https://www.google.com/search?q=%s").value, this)
}

data class NameValue(val name: String, val value: String)

val String.host: String?
    get() {
        val uri = if (URLUtil.isValidUrl(this)) {
            Uri.parse(this)
        } else {
            Uri.parse("https://$this")
        }
        return uri.host
    }