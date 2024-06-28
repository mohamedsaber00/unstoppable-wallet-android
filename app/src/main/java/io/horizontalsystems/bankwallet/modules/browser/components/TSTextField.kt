package io.horizontalsystems.bankwallet.modules.browser.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.body_grey50

@Composable
fun TSTextField(
    modifier: Modifier = Modifier,
    text: MutableState<TextFieldValue>,
    placeholder: String = "",
    onEnter: () -> Unit = {},
    onFocusChanged: (FocusState) -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(onGo = { onEnter() }),
    onValueChanged: () -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .border(1.dp, ComposeAppTheme.colors.lightGrey, RoundedCornerShape(8.dp))
            ,
        )
        OutlinedTextField(
            value = text.value,
            placeholder = {
                body_grey50(
                    text = placeholder,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = ComposeAppTheme.colors.jacob,
                textColor = ComposeAppTheme.colors.leah
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent { event ->
                    if (event.key == Key.Enter) {
                        onEnter()
                        return@onKeyEvent true
                    }
                    false
                }
                .onFocusChanged { state ->
                    onFocusChanged(state)
                },
            leadingIcon = leadingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            onValueChange = {
                text.value = it
                onValueChanged()
            },
        )
    }
}