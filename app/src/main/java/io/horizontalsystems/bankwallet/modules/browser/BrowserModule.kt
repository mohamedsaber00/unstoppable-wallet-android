package io.horizontalsystems.bankwallet.modules.browser

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.modules.releasenotes.ReleaseNotesViewModel

object BrowserModule {


    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BrowserViewModel(
                App.networkManager,
            ) as T
        }
    }


    enum class Tab(@StringRes val titleResId: Int) {
        Apps(R.string.Apps_tab),
        Tokens(R.string.Token_tab),
        Collection(R.string.Collection_tab),
        Quests(R.string.Quests_tab),
        Learn(R.string.Learn_tab);
//        Sectors(R.string.Market_Tab_Sectors);

        companion object {
            private val map = entries.associateBy(Tab::name)

            fun fromString(type: String?): Tab? = map[type]
        }
    }

}