package io.horizontalsystems.bankwallet.modules.browser

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.modules.market.SortingField
import io.horizontalsystems.bankwallet.modules.market.TopMarket
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
fun NewTabPage(navController: NavController) {
    val context = LocalContext.current

    Surface(color = ComposeAppTheme.colors.tyler) {
        BrowserScreen(navController = navController)
    }
}