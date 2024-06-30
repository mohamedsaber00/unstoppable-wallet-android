package io.horizontalsystems.bankwallet.modules.browser.tab

import android.graphics.Bitmap
import android.os.Message
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.mutableStateOf
import io.horizontalsystems.bankwallet.core.utils.mainScope
import io.horizontalsystems.bankwallet.core.web.TSWebView
import io.horizontalsystems.bankwallet.core.web.WebDataListener
import io.horizontalsystems.bankwallet.entities.TabInfo
import kotlinx.coroutines.launch

data class Tab(
    val info: TabInfo,
    var view: TSWebView,
) : WebDataListener {

    var parentTab: Tab? = null

    override val progressState = mutableStateOf(0f)
    override val titleState = mutableStateOf("")
    override val previewState = mutableStateOf<Bitmap?>(null)

    val urlState = mutableStateOf("")
    val canGoBackState = mutableStateOf(false)
    val canGoForwardState = mutableStateOf(false)

    // val tempHistoryList = mutableListOf<History>()

    override fun onCreateWindow(message: Message) {
        message.apply {
            val newWebView = WebView(view.context)
            newWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    val url = request.url
                    TabManager.newTab(view.context).apply {
                        parentTab = this@Tab
                        loadUrl(url.toString())
                        active()
                        canGoBackState.value = true
                    }
                    return true
                }
            }
            (obj as WebView.WebViewTransport).webView = newWebView
        }.sendToTarget()
    }

    override fun onCloseWindow() {
        TabManager.remove(this)
    }

    override fun onReceivedIcon(icon: Bitmap?) {
        val url = urlState.value

        /*        mainScope.launch {
                    val search = SearchHistory(
                        view.originalUrl ?: "",
                        System.currentTimeMillis()
                    )
                    search.title = titleState.value
                    search.url = url
                    val dao = AppDatabase.instance.searchHistoryDao()
                    if (dao.getByName(search.query) != null) {
                        dao.update(search)
                    }
                }*/
    }

    override fun doUpdateVisitedHistory(url: String, isReload: Boolean) {
        urlState.value = url
        canGoBackState.value = true
        canGoForwardState.value = view.canGoForward()
        mainScope.launch {
            val title = titleState.value
            /*            if (URLUtil.isNetworkUrl(url) && title.isNotBlank() && title != App.instance.getString(R.string.new_tab)) {
                            val last = AppDatabase.instance.historyDao().last()
                            if (url != last?.url && title != last?.title) {
                                val history = History(
                                    url = url,
                                    title = title,
                                    date = System.currentTimeMillis()
                                )
                                if (!Settings.incognito) {
                                    AppDatabase.instance.historyDao().insert(history).apply {
                                        history.id = this
                                        tempHistoryList.add(history)
                                    }
                                }
                            }
                        }*/
        }
        view.generatePreview()
    }

    init {
        view.dataListener = this
    }

    fun loadUrl(url: String) {
        urlState.value = url
        view.post {
            view.loadUrl(url)
        }
    }

    val isHome
        get() = urlState.value == "about:blank"

    fun goHome() {
        view.post {
            view.loadUrl("about:blank")
        }
    }

    fun onResume() {
        view.onResume()
    }

    fun onPause() {
//        view.generatePreview()
        view.onPause()
    }

    fun onBackPressed(): Boolean {
        if (view.canGoBack()) {
            view.goBack()
            return true
        }
        parentTab?.let {
            TabManager.remove(this)
            TabManager.active(it)
            return true
        }
        if (TabManager.currentTab.value?.isHome == true) {
            return false
        }
        return false
    }

    fun goForward() {
        if (view.canGoForward()) {
            view.goForward()
        }
    }

    override suspend fun updateInfo() {
        /*     info.url = urlState.value
             info.thumbnailPath = previewState.value?.encodeToPath("preview-${info.url}")
             info.title = titleState.value
             if (!Settings.incognito) {
                 info.update()
             }*/
    }
}