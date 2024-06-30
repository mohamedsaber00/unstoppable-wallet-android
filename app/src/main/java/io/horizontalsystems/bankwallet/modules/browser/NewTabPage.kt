package io.horizontalsystems.bankwallet.modules.browser

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
fun NewTabPage() {
    Surface(color = ComposeAppTheme.colors.tyler) {
        BrowserScreen()
    }
}