package io.horizontalsystems.bankwallet.modules.browser

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
fun NewTabPage() {
    val context = LocalContext.current

    Surface(color = ComposeAppTheme.colors.tyler) {
        BrowserScreen(navController = NavController(context))
    }
}