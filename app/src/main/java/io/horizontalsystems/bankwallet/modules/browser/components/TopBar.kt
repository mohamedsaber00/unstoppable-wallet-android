package io.horizontalsystems.bankwallet.modules.browser.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.core.utils.logD
import io.horizontalsystems.bankwallet.modules.browser.BrowserScreen
import io.horizontalsystems.bankwallet.modules.browser.BrowserUIState
import io.horizontalsystems.bankwallet.modules.browser.LocalViewModel
import io.horizontalsystems.bankwallet.modules.browser.tab.TabManager
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
fun TopBar() {
    val viewModel = LocalViewModel.current
    val tab = TabManager.currentTab.value
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .graphicsLayer {
                translationY = -viewModel.imeHeightState.value
            }) {
        val showNewPage = uiState.value == BrowserUIState.Main && tab?.isHome != false

        Row() {
            AnimatedVisibility(visible = showNewPage) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(56.dp)
                        .background(MaterialTheme.colors.surface),
                ) {
                    logD("showNewPage $showNewPage ,,, uiState ${uiState.value}")
                    TabButton(uiState)
                }
            }

            AnimatedVisibility(visible = uiState.value == BrowserUIState.Search || uiState.value == BrowserUIState.Main) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(56.dp)
                        .background(ComposeAppTheme.colors.dark),
                ) {
                    if (uiState.value != BrowserUIState.TabList) {
                        AddressTextField(
                            modifier = Modifier.weight(1f),
                            uiState = uiState
                        )
                    }
                }
            }
        }



        AnimatedVisibility(visible = !showNewPage) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .background(MaterialTheme.colors.surface)
                    .padding(start = if (uiState.value == BrowserUIState.Search) 8.dp else 0.dp),
            ) {
                AnimatedVisibility(visible = uiState.value == BrowserUIState.Main) {
                    HomeButton()
                }
                AnimatedVisibility(visible = uiState.value == BrowserUIState.Main) {
                    TabButton(uiState)
                }
                AnimatedVisibility(visible = uiState.value == BrowserUIState.TabList) {
                    CloseAll()
                }
                AnimatedVisibility(visible = uiState.value == BrowserUIState.TabList) {
                    NewTab(uiState)
                }
                AnimatedVisibility(visible = true, modifier = Modifier.weight(1f)) {

                }
                AnimatedVisibility(visible = uiState.value == BrowserUIState.Search) {
                    //   CancelButton(uiState)
                }
                AnimatedVisibility(visible = uiState.value == BrowserUIState.Main) {
                    RefreshButton()
                }
                AnimatedVisibility(visible = uiState.value != BrowserUIState.Search) {
                    //  MoreButton(drawerState)
                }
            }
        }
    }
}
