package io.horizontalsystems.bankwallet.modules.browser.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.modules.browser.BrowserUIState
import io.horizontalsystems.bankwallet.modules.browser.LocalViewModel
import io.horizontalsystems.bankwallet.modules.browser.tab.TabManager

@Composable
fun TopBar() {
    val viewModel = LocalViewModel.current
    val tab = TabManager.currentTab.value
    val uiState = viewModel.browseUiState

    Column(modifier = Modifier.graphicsLayer {
        translationY = -viewModel.imeHeightState.value
    }) {
        val showNewPage = uiState.value == BrowserUIState.Main && tab?.isHome != false
            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(
                    modifier = Modifier
                        .weight(if (showNewPage) 0.85f else 1f),
                     //   .background(MaterialTheme.colors.surface),
                    visible = uiState.value == BrowserUIState.Search || uiState.value == BrowserUIState.Main
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(56.dp)
                     //       .background(MaterialTheme.colors.surface),
                    ) {
                        if (uiState.value != BrowserUIState.TabList) {
                            AddressTextField(
                                modifier = Modifier.fillMaxWidth(),  // Ensure this fills the allocated space
                                uiState = uiState
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    //      modifier = Modifier.weight(0.15f), // Assign a fixed weight to the button
                    visible = uiState.value == BrowserUIState.Main
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(56.dp)
                            .weight(0.15f)  // Assign a fixed weight to the button
                        //    .background(MaterialTheme.colors.surface),
                    ) {
                        TabButton(uiState)
                    }
                }
            }
        }
}