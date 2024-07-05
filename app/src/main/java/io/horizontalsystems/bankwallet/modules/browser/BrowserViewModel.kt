package io.horizontalsystems.bankwallet.modules.browser

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.horizontalsystems.bankwallet.core.App.Companion.currencyManager
import io.horizontalsystems.bankwallet.core.App.Companion.marketKit
import io.horizontalsystems.bankwallet.core.INetworkManager
import io.horizontalsystems.bankwallet.core.ViewModelUiState
import io.horizontalsystems.bankwallet.core.managers.CurrencyManager
import io.horizontalsystems.bankwallet.core.managers.MarketFavoritesManager
import io.horizontalsystems.bankwallet.core.managers.MarketKitWrapper
import io.horizontalsystems.bankwallet.core.web.util.toUrl
import io.horizontalsystems.bankwallet.entities.ViewState
import io.horizontalsystems.bankwallet.entities.browse.CollectionsResponse
import io.horizontalsystems.bankwallet.entities.browse.DAppResponse
import io.horizontalsystems.bankwallet.modules.browser.tab.TabManager
import io.horizontalsystems.bankwallet.modules.market.MarketItem
import io.horizontalsystems.bankwallet.modules.market.MarketModule.Tab
import io.horizontalsystems.bankwallet.modules.market.MarketViewItem
import io.horizontalsystems.bankwallet.modules.market.SortingField
import io.horizontalsystems.bankwallet.modules.market.TimeDuration
import io.horizontalsystems.bankwallet.modules.market.TopMarket
import io.horizontalsystems.bankwallet.modules.market.favorites.period
import io.horizontalsystems.bankwallet.modules.market.sort
import io.horizontalsystems.bankwallet.modules.market.topcoins.MarketTopCoinsUiState
import io.horizontalsystems.marketkit.models.MarketInfo
import io.horizontalsystems.marketkit.models.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.asFlow
import kotlinx.coroutines.rx2.await
import kotlin.enums.EnumEntries
import kotlin.math.min

class BrowserViewModel(
    private val iNetworkManager: INetworkManager,
    private var topMarket: TopMarket,
    private var sortingField: SortingField,
    private val marketKit: MarketKitWrapper,
    private val currencyManager: CurrencyManager,
    private val favoritesManager: MarketFavoritesManager,
) : ViewModelUiState<TokensUiState>() {

    val browseUiState = mutableStateOf(BrowserUIState.Main)

    private val periods = listOf(
        TimeDuration.OneDay,
        TimeDuration.SevenDay,
        TimeDuration.ThirtyDay,
        TimeDuration.ThreeMonths,
    )
    private val sortingFields = listOf(
        SortingField.HighestCap,
        SortingField.LowestCap,
        SortingField.TopGainers,
        SortingField.TopLosers,
    )
    private val topMarkets = TopMarket.entries
    private val baseCurrency get() = currencyManager.baseCurrency

    private var isRefreshing = false
    private var viewState: ViewState = ViewState.Loading
    private var viewItems: List<MarketViewItem> = listOf()
    private var period = periods[0]
    private var favoriteCoinUids: List<String> = listOf()

    private var marketInfoList: List<MarketInfo>? = null
    private var marketItemList: List<MarketItem>? = null
    private var sortedMarketItems: List<MarketItem>? = null

    private val TAG = "BrowserViewModel"
    //  val searchList = mutableStateListOf<SearchHistory>()
    val addressText = mutableStateOf(TextFieldValue())
    val imeHeightState = Animatable(0f)
    val pageTabs = BrowserModule.BrowserTabs.entries.toTypedArray()

    private var _dAppsResponse: MutableStateFlow<DAppResponse?> = MutableStateFlow(null)
    val dAppsResponse = _dAppsResponse.asStateFlow()

    private var _collectionsResponse: MutableStateFlow<CollectionsResponse?> =
        MutableStateFlow(null)
    val collectionsResponse = _collectionsResponse.asStateFlow()

    init {
        getDApps()
        getCollections()
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


    private fun getDApps() {
        viewModelScope.launch {
            _dAppsResponse.value = iNetworkManager.getDApps()
            Log.d(TAG, "getDApps: ${dAppsResponse.value?.ads}")

        }
    }

    private fun getCollections() {
        viewModelScope.launch {
            _collectionsResponse.value = iNetworkManager.getCollections()

        }
    }

    override fun createState() = TokensUiState(
        isRefreshing = isRefreshing,
        viewState = viewState,
        viewItems = viewItems,
        topMarkets = topMarkets,
        topMarket = topMarket,
        sortingFields = sortingFields,
        sortingField = sortingField,
        periods = periods,
        period = period,
    )

    init {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                reload()

                viewState = ViewState.Success
            } catch (e: Throwable) {
                viewState = ViewState.Error(e)
            }

            emitState()
        }

        viewModelScope.launch(Dispatchers.Default) {
            favoritesManager.dataUpdatedAsync.asFlow().collect {
                refreshFavoriteCoinUids()
                refreshViewItems()

                emitState()
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            currencyManager.baseCurrencyUpdatedFlow.collect {
                try {
                    reload()

                    viewState = ViewState.Success
                } catch (e: Throwable) {
                    viewState = ViewState.Error(e)
                }

                emitState()
            }
        }
    }

    private suspend fun reload() {
        fetchMarketInfoList()

        refreshFavoriteCoinUids()
        refreshMarketItemList()
        refreshSortedMarketItems()
        refreshViewItems()
    }

    private fun refreshFavoriteCoinUids() {
        favoriteCoinUids = favoritesManager.getAll().map { it.coinUid }
    }

    private suspend fun fetchMarketInfoList() {
        marketInfoList = marketKit.topCoinsMarketInfosSingle(500, baseCurrency.code).await()
    }

    private fun refreshMarketItemList() {
        marketItemList = marketInfoList?.map { marketInfo ->
            MarketItem.createFromCoinMarket(
                marketInfo,
                baseCurrency,
                period.period,
            )
        }
    }

    private fun refreshSortedMarketItems() {
        sortedMarketItems = marketItemList?.let { list ->
            list
                .subList(0, min(list.size, topMarket.value))
                .sort(sortingField)
        }
    }

    private fun refreshViewItems() {
        sortedMarketItems?.let { list ->
            viewItems = list.map {
                MarketViewItem.create(it, favoriteCoinUids.contains(it.fullCoin.coin.uid))
            }
        }
    }

    fun refresh() {
        isRefreshing = true
        emitState()

        viewModelScope.launch(Dispatchers.Default) {
            try {
                reload()

                viewState = ViewState.Success
            } catch (e: Throwable) {
                viewState = ViewState.Error(e)
            }

            isRefreshing = false
            emitState()
        }
    }

    fun onAddFavorite(uid: String) {
        viewModelScope.launch(Dispatchers.Default) {
            favoritesManager.add(uid)
        }
    }

    fun onRemoveFavorite(uid: String) {
        viewModelScope.launch(Dispatchers.Default) {
            favoritesManager.remove(uid)
        }
    }

    fun onSelectSortingField(sortingField: SortingField) {
        this.sortingField = sortingField
        emitState()

        viewModelScope.launch(Dispatchers.Default) {
            refreshSortedMarketItems()
            refreshViewItems()

            emitState()
        }
    }

    fun onSelectTopMarket(topMarket: TopMarket) {
        this.topMarket = topMarket
        emitState()

        viewModelScope.launch(Dispatchers.Default) {
            refreshSortedMarketItems()
            refreshViewItems()

            emitState()
        }
    }

    fun onSelectPeriod(period: TimeDuration) {
        this.period = period
        emitState()

        viewModelScope.launch(Dispatchers.Default) {
            refreshMarketItemList()
            refreshSortedMarketItems()
            refreshViewItems()

            emitState()
        }
    }


}

val LocalViewModel = staticCompositionLocalOf<BrowserViewModel> {
    error("no view model provided")
}

data class TokensUiState(
    val isRefreshing: Boolean,
    val viewState: ViewState,
    val viewItems: List<MarketViewItem>,
    val topMarkets: EnumEntries<TopMarket>,
    val topMarket: TopMarket,
    val sortingFields: List<SortingField>,
    val sortingField: SortingField,
    val periods: List<TimeDuration>,
    val period: TimeDuration,
)