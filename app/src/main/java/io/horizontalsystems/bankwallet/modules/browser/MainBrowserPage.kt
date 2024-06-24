package io.horizontalsystems.bankwallet.modules.browser

import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.core.utils.logD
import io.horizontalsystems.bankwallet.core.utils.removeFromParent
import io.horizontalsystems.bankwallet.modules.browser.components.TopBar
import io.horizontalsystems.bankwallet.modules.browser.components.TSBackHandler
import io.horizontalsystems.bankwallet.modules.browser.tab.TabList
import io.horizontalsystems.bankwallet.modules.browser.tab.TabManager


@Composable
fun MainBrowserPage(
    navController: NavController,
    browserViewModel: BrowserViewModel
) {
    CompositionLocalProvider(
        LocalViewModel provides browserViewModel,
        content = {
            MainView()
        }
    )
}

@Composable
fun MainView() {
    logD("MainView start")
    val tab = TabManager.currentTab.value

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar()
        Box(modifier = Modifier.weight(1f)) {
            TSBackHandler(
                enabled = tab?.canGoBackState?.value == true,
                onBack = { tab?.onBackPressed() }) {
                AndroidView(
                    factory = {
                        FrameLayout(it)
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { tabContainer ->
                        tab?.let {
                            tabContainer.removeAllViews()
                            it.view.removeFromParent()
                            tabContainer.addView(it.view)
                        }
                    }
                )
            }
            ProgressIndicator()
            NewTabView()
            CoverView()
        }
    }
    logD("MainView end")
}

@Composable
fun NewTabView() {
    val tab = TabManager.currentTab.value ?: return
    val viewModel = LocalViewModel.current
    val uiState = viewModel.uiState
    if (uiState.value == BrowserUIState.Main && tab.isHome) {
        NewTabPage()
    }
}

@Composable
fun CoverView() {
    val viewModel = LocalViewModel.current
    val uiState = viewModel.uiState
    TSBackHandler(
        enabled = uiState.value != BrowserUIState.Main,
        onBack = { viewModel.uiState.value = BrowserUIState.Main }) {
        when (uiState.value) {
            BrowserUIState.Search -> SearchList()
            BrowserUIState.TabList -> TabList()
            else -> {
            }
        }
    }
}

@Composable
fun ProgressIndicator() {
    val currentTab = TabManager.currentTab
    val progressState = currentTab.value?.progressState
    val newProgress = progressState?.value ?: 0f
    val progress: Float = if (newProgress > 0) {
        animateFloatAsState(targetValue = newProgress).value
    } else {
        newProgress
    }
    AnimatedVisibility(visible = progress > 0f && progress < 1f) {
        LinearProgressIndicator(
            progress = progress,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}