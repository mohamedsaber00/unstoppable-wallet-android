package io.horizontalsystems.bankwallet.modules.browser

import androidx.annotation.StringRes
import io.horizontalsystems.bankwallet.R

object BrowserModule {


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