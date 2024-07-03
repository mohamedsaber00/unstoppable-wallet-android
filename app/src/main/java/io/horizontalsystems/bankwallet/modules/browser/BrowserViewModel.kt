package io.horizontalsystems.bankwallet.modules.browser

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.horizontalsystems.bankwallet.core.INetworkManager
import io.horizontalsystems.bankwallet.core.managers.DAppsService
import io.horizontalsystems.bankwallet.core.utils.logD
import io.horizontalsystems.bankwallet.core.web.util.toUrl
import io.horizontalsystems.bankwallet.entities.browse.Ads
import io.horizontalsystems.bankwallet.entities.browse.DAppResponse
import io.horizontalsystems.bankwallet.entities.browse.Marketing
import io.horizontalsystems.bankwallet.modules.browser.tab.TabManager
import io.horizontalsystems.bankwallet.modules.browser.tab.active
import io.horizontalsystems.bankwallet.modules.market.MarketModule.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.collect
import timber.log.Timber
import kotlin.math.log

class BrowserViewModel(
    private val iNetworkManager: INetworkManager
) : ViewModel() {
    val uiState = mutableStateOf(BrowserUIState.Main)

    
    private val TAG = "BrowserViewModel"

    //  val searchList = mutableStateListOf<SearchHistory>()
    val addressText = mutableStateOf(TextFieldValue())
    val imeHeightState = Animatable(0f)
    val pageTabs = Tab.entries.toTypedArray()

    private var _dAppsResponse: MutableStateFlow<DAppResponse?> = MutableStateFlow(null)
    val dAppsResponse = _dAppsResponse.asStateFlow()

    init {
        getDApps()
    }


    fun onGo(text: String, context: Context) {
        val urlText = text.trim()
        if (urlText.isBlank()) {
            return
        }
        TabManager.currentTab.value?.loadUrl(urlText.toUrl())
        editInAddressBar(urlText.toUrl())
    }

    fun resetTab() {
        TabManager.currentTab.value?.goHome()
    }


    fun editInAddressBar(url: String) {
        addressText.value = TextFieldValue(url, TextRange(url.length, url.length))
    }


    fun getDApps() {
        viewModelScope.launch {
            _dAppsResponse.value = iNetworkManager.getDApps()
            Log.d(TAG, "getDApps: ${_dAppsResponse.value?.ads?.marketing}")

        }
    }

}

val LocalViewModel = staticCompositionLocalOf<BrowserViewModel> {
    error("no view model provided")
}