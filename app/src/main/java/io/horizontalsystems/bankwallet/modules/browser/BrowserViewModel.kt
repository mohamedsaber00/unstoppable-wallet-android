package io.horizontalsystems.bankwallet.modules.browser

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import io.horizontalsystems.bankwallet.core.utils.logD
import io.horizontalsystems.bankwallet.core.web.util.toUrl
import io.horizontalsystems.bankwallet.modules.browser.tab.TabManager
import io.horizontalsystems.bankwallet.modules.browser.tab.active
import io.horizontalsystems.bankwallet.modules.market.MarketModule.Tab

class BrowserViewModel : ViewModel() {
    val uiState = mutableStateOf(BrowserUIState.Main)

    //  val searchList = mutableStateListOf<SearchHistory>()
    val addressText = mutableStateOf(TextFieldValue())
    val imeHeightState = Animatable(0f)
    val pageTabs = Tab.entries.toTypedArray()



    fun onGo(text: String, context: Context) {
        val urlText = text.trim()
        if (urlText.isBlank()) {
            return
        }

        logD("onGo here")
        if (TabManager.currentTab.value == null) {
            TabManager.newTab(context).apply {
                active()
                loadUrl(urlText.toUrl())
            }
        } else TabManager.currentTab.value?.loadUrl(urlText.toUrl())
         editInAddressBar(urlText.toUrl())
    }


    fun editInAddressBar(url: String) {
        addressText.value = TextFieldValue(url, TextRange(url.length, url.length))
    }

}

val LocalViewModel = staticCompositionLocalOf<BrowserViewModel> {
    error("no view model provided")
}