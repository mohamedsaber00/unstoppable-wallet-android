package io.horizontalsystems.bankwallet.modules.browser.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.browser.BrowserUIState
import io.horizontalsystems.bankwallet.modules.browser.LocalViewModel
import io.horizontalsystems.bankwallet.modules.browser.tab.TabManager
import io.horizontalsystems.bankwallet.modules.browser.tab.active
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TabButton(uiState: MutableState<BrowserUIState>) {
    val tabs = TabManager.tabs
    IconButton(onClick = {
        TabManager.currentTab.value?.view?.generatePreview()
        uiState.value = BrowserUIState.TabList
    }) {

        Box(
            modifier = Modifier
                .border(
                    1.5.dp,
                    color = ComposeAppTheme.colors.jacob,
                    RoundedCornerShape(4.dp)
                )
                .size(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (tabs.size == 0) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_yellow),
                    tint = ComposeAppTheme.colors.jacob,
                    contentDescription = null
                )
            } else {
                Text(
                    modifier = Modifier.padding(bottom = 1.dp),
                    text = tabs.size.toString(),
                    fontSize = 12.sp,
                    color = ComposeAppTheme.colors.jacob,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun CloseAll() {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .clickable {
                TabManager.removeAll()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(imageVector = Icons.Default.Close, contentDescription = "Close all")
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = R.string.closeAll))
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun NewTab(uiState: MutableState<BrowserUIState>) {
    val context = LocalContext.current
   val viewmodel =  LocalViewModel.current
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 4.dp)
            .clickable {
                TabManager
                    .newTab(context)
                    .apply {
                        goHome()
                        active()
                        viewmodel.editInAddressBar(urlState.value)
                    }
                uiState.value = BrowserUIState.Main
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(imageVector = Icons.Default.Add, contentDescription = "New tab")
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(id = R.string.new_tab))
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun CancelButton(uiState: MutableState<BrowserUIState>) {
    val viewModel = LocalViewModel.current
    val scope = rememberCoroutineScope()
    IconButton(
        modifier = Modifier.widthIn(56.dp, 72.dp),
        onClick = {
            uiState.value = BrowserUIState.Main
            scope.launch {
                //FIXME why need to delay
                delay(50)
                viewModel.addressText.value = TextFieldValue()
            }
        }
    ) {
        Text(text = stringResource(id = R.string.action_cancel))
    }

}


@Composable
fun HomeButton() {
    IconButton(onClick = {
        TabManager.currentTab.value?.goHome()
    }) {
        Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home")
    }
}

@Composable
fun RefreshButton() {
    val tab by TabManager.currentTab
    val progress = tab?.progressState?.value
    IconButton(
        onClick = {
            if (progress == 1f) {
                tab?.view?.reload()
            } else {
                tab?.view?.stopLoading()
            }
        },
        enabled = tab != null
    ) {
        Icon(
            imageVector = if (progress == 1f) Icons.Outlined.Refresh else Icons.Outlined.Close,
            contentDescription = "Refresh"
        )
    }
}

