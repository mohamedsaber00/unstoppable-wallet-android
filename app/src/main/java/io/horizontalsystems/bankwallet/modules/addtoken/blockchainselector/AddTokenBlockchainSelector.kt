package io.horizontalsystems.bankwallet.modules.addtoken.blockchainselector

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.imageUrl
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.AppBar
import io.horizontalsystems.bankwallet.ui.compose.components.CellBorderedRowUniversal
import io.horizontalsystems.bankwallet.ui.compose.components.HsBackButton
import io.horizontalsystems.bankwallet.ui.compose.components.body_leah
import io.horizontalsystems.bankwallet.ui.compose.components.cell.CellBlockchainChecked
import io.horizontalsystems.bankwallet.ui.compose.components.cell.SectionUniversalLawrence
import io.horizontalsystems.marketkit.models.Blockchain

const val BlockchainSelectorResult = "blockchain_selector_result_key"

@Composable
fun AddTokenBlockchainSelectorScreen(
    blockchains: List<Blockchain>,
    selectedBlockchain: Blockchain,
    navController: NavController,
) {
    var selectedItem = selectedBlockchain

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.Market_Filter_Blockchains),
                navigationIcon = {
                    HsBackButton(onClick = { navController.popBackStack() })
                },
            )
        },
        backgroundColor = ComposeAppTheme.colors.tyler
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            SectionUniversalLawrence {
                blockchains.forEachIndexed { index, item ->
                    CellBlockchainChecked(
                        borderTop = index != 0,
                        blockchain = item,
                        checked = selectedItem == item,
                    ) {
                        selectedItem = item
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(BlockchainSelectorResult, listOf(item))
                        navController.popBackStack()
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun BlockchainCell(
    item: Blockchain,
    selected: Boolean,
    onCheck: (Blockchain) -> Unit,
    borderTop: Boolean
) {
    CellBorderedRowUniversal(
        borderTop = borderTop,
        modifier = Modifier
            .clickable {
                onCheck(item)
            }
            .fillMaxWidth(),
        backgroundColor = ComposeAppTheme.colors.lawrence
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = item.type.imageUrl,
                error = painterResource(R.drawable.ic_platform_placeholder_32)
            ),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        body_leah(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f),
            text = item.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (selected) {
            Icon(
                painter = painterResource(R.drawable.ic_checkmark_20),
                tint = ComposeAppTheme.colors.jacob,
                contentDescription = null,
            )
        }
    }
}
