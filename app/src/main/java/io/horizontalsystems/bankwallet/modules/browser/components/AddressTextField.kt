package io.horizontalsystems.bankwallet.modules.browser.components

import android.webkit.URLUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.browser.BrowserUIState
import io.horizontalsystems.bankwallet.modules.browser.LocalViewModel
import io.horizontalsystems.bankwallet.modules.browser.tab.TabManager
import io.horizontalsystems.bankwallet.modules.browser.util.IconMap
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import kotlinx.coroutines.launch


@Composable
fun AddressTextField(modifier: Modifier, uiState: MutableState<BrowserUIState>) {
    val viewModel = LocalViewModel.current
    val text = viewModel.addressText
    val tab = TabManager.currentTab.value
    val url = tab?.urlState?.value
    val title = tab?.titleState?.value
    val icon = IconMap[url ?: ""]
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val focusRequester = FocusRequester()
    if (uiState.value != BrowserUIState.Search) {
        focusManager.clearFocus()
    }
    val context = LocalContext.current

    val placeholder = if (title?.isNotBlank() == true && title != url) {
        title
    } else if (URLUtil.isNetworkUrl(url)) {
        url
    } else null

    fun onGo() {
        uiState.value = BrowserUIState.Main
        viewModel.onGo(text.value.text, context)
        text.value = TextFieldValue()
        focusManager.clearFocus()
    }

    TSTextField(
        modifier = modifier
            .focusRequester(focusRequester),
        text = text,
        placeholder = placeholder ?: stringResource(id = R.string.address_bar),
        onEnter = { onGo() },
        onFocusChanged = { state ->
            if (state.isFocused) {
                uiState.value = BrowserUIState.Search
            } else if (uiState.value == BrowserUIState.Search) {
                scope.launch {
                    focusRequester.requestFocus()
                }
            }
        },
        leadingIcon = when (uiState.value) {
            BrowserUIState.Search -> {
                {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                    ) }
            }
            else -> {
                {
                    icon?.asImageBitmap()?.let {
                        Image(
                            bitmap = it,
                            contentDescription = url,
                            modifier = Modifier.size(20.dp),
                        )
                    } ?: Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go)
    )
}