package io.horizontalsystems.bankwallet.modules.browser

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.market.SortingField
import io.horizontalsystems.bankwallet.modules.market.TopMarket

object BrowserModule {


    class Factory(
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BrowserViewModel(
                App.networkManager,
//                topMarket,
//                sortingField,
//                App.marketKit,
//                App.currencyManager,
//                App.marketFavoritesManager
            ) as T
        }
    }


    enum class BrowserTabs(@StringRes val titleResId: Int) {
        Apps(R.string.Apps_tab),
        Tokens(R.string.Token_tab),
        Collection(R.string.Collection_tab),
        Quests(R.string.Quests_tab),
        Learn(R.string.Learn_tab);
//        Sectors(R.string.Market_Tab_Sectors);

        companion object {
            private val map = entries.associateBy(BrowserTabs::name)

            fun fromString(type: String?): BrowserTabs? = map[type]
        }
    }

}