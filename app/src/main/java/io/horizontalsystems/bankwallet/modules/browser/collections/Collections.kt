package io.horizontalsystems.bankwallet.modules.browser.collections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import io.horizontalsystems.bankwallet.core.utils.formatToTwoDecimalPlaces
import io.horizontalsystems.bankwallet.core.utils.withSign
import io.horizontalsystems.bankwallet.entities.browse.CollectionsMarketing
import io.horizontalsystems.bankwallet.entities.browse.CollectionsNFT
import io.horizontalsystems.bankwallet.modules.browser.BrowserModule
import io.horizontalsystems.bankwallet.modules.browser.BrowserScreen
import io.horizontalsystems.bankwallet.modules.browser.BrowserViewModel
import io.horizontalsystems.bankwallet.modules.browser.LocalViewModel
import io.horizontalsystems.bankwallet.modules.browser.NetworkSample
import io.horizontalsystems.bankwallet.modules.browser.NetworkState
import io.horizontalsystems.bankwallet.modules.browser.NetworkType
import io.horizontalsystems.bankwallet.modules.browser.TypeDropDown
import io.horizontalsystems.bankwallet.modules.browser.VerticalAppListParent
import io.horizontalsystems.bankwallet.modules.market.SortingField
import io.horizontalsystems.bankwallet.modules.market.TopMarket
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.body_leah
import io.horizontalsystems.bankwallet.ui.compose.components.caption_leah
import io.horizontalsystems.bankwallet.ui.compose.components.caption_lucian
import io.horizontalsystems.bankwallet.ui.compose.components.caption_remus
import io.horizontalsystems.bankwallet.ui.compose.components.subhead2_grey


@Composable
fun Collections() {
    val browserViewModel: BrowserViewModel = viewModel(factory = BrowserModule.Factory(
        TopMarket.Top100,
        SortingField.TopGainers
    ))

    Column()
    {
        browserViewModel.collectionsResponse.collectAsState().value?.ads?.let {
            HorizontalCollectionList(
                it.marketing
            )
        }
        VerticalCollectionsListParent(browserViewModel)
    }

}


@Composable
fun HorizontalCollectionList(
    featuredApps: List<CollectionsMarketing>
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        featuredApps.forEach {
            FeaturedCollectionsCard(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun VerticalCollectionList(trendApps: List<CollectionsNFT>?) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Spacer(modifier = Modifier.width(8.dp))
        }

        trendApps?.forEach {
            TrendingCollectionsItem(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun VerticalCollectionsListParent(
    browserViewModel: BrowserViewModel
) {
    var selectedNetworkType by remember { mutableStateOf(NetworkType.ALL_NETWORKS) }
    var selectedNetworkState by remember { mutableStateOf(NetworkState.TREND) }

    val filteredApps =
        when {
            selectedNetworkState == NetworkState.TREND && selectedNetworkType == NetworkType.ALL_NETWORKS ->
                browserViewModel.collectionsResponse.collectAsState().value?.trend?.all

            selectedNetworkState == NetworkState.TREND && selectedNetworkType == NetworkType.SOLANA ->
                browserViewModel.collectionsResponse.collectAsState().value?.trend?.solana

            selectedNetworkState == NetworkState.TREND && selectedNetworkType == NetworkType.ETHEREUM ->
                browserViewModel.collectionsResponse.collectAsState().value?.trend?.ethereum

            selectedNetworkState == NetworkState.TREND && selectedNetworkType == NetworkType.POLYGON ->
                browserViewModel.collectionsResponse.collectAsState().value?.trend?.polygon

            selectedNetworkState == NetworkState.TOP && selectedNetworkType == NetworkType.ALL_NETWORKS ->
                browserViewModel.collectionsResponse.collectAsState().value?.top?.all

            selectedNetworkState == NetworkState.TOP && selectedNetworkType == NetworkType.SOLANA ->
                browserViewModel.collectionsResponse.collectAsState().value?.top?.solana

            selectedNetworkState == NetworkState.TOP && selectedNetworkType == NetworkType.ETHEREUM ->
                browserViewModel.collectionsResponse.collectAsState().value?.top?.ethereum

            else -> browserViewModel.collectionsResponse.collectAsState().value?.top?.polygon

        }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            TypeDropDown(selectedNetworkState) { newNetworkState ->
                selectedNetworkState = newNetworkState
            }
            Spacer(modifier = Modifier.width(8.dp))
            NetworkSample(selectedNetworkType) { newNetworkType ->
                selectedNetworkType = newNetworkType
            }
        }
        InfoRow()
        VerticalCollectionList(filteredApps)
    }
}


@Composable
fun FeaturedCollectionsCard(collectionsMarketing: CollectionsMarketing) {
    val viewModel = LocalViewModel.current
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(200.dp)
            .clickable {
                viewModel.onGo(collectionsMarketing.url, context = context)
            },
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ComposeAppTheme.colors.lawrence)
                .padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(collectionsMarketing.floor),
                contentDescription = collectionsMarketing.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Column(Modifier.weight(1f)) {
                    body_leah(
                        text = collectionsMarketing.name
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    collectionsMarketing.floor.formatToTwoDecimalPlaces()?.let {
                        subhead2_grey(
                            text = it
                        )
                    }
                }

                Image(
                    painter = rememberAsyncImagePainter(collectionsMarketing.icon),
                    contentDescription = collectionsMarketing.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                )


            }

        }


    }
}

@Composable
fun TrendingCollectionsItem(collectionsNft: CollectionsNFT) {
    val viewModel = LocalViewModel.current
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                viewModel.onGo(collectionsNft.url, context)
            },

        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${collectionsNft.id}",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.width(24.dp),
            color = ComposeAppTheme.colors.lightGrey
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = rememberAsyncImagePainter(collectionsNft.icon),
            contentDescription = collectionsNft.name,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            body_leah(
                modifier = Modifier.width(150.dp),
                text = collectionsNft.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            collectionsNft.floor.formatToTwoDecimalPlaces()?.let {
                subhead2_grey(
                    text = it
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.End
        ) {
            collectionsNft.volume.formatToTwoDecimalPlaces()?.let {
                body_leah(
                    text = it,
                )
            }

            if (collectionsNft.volume_change > 0){
                collectionsNft.volume_change.withSign()?.let {
                    caption_remus(
                        text = it,
                        overflow = TextOverflow.Clip
                    )
                }
            }
            else if(collectionsNft.volume_change < 0){
                collectionsNft.volume_change.withSign()?.let {
                    caption_lucian(
                        text = it,
                        overflow = TextOverflow.Clip

                    )
                }
            }



        }


    }
}

@Composable
fun InfoRow() {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .height(24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        subhead2_grey(
            text = "#"
        )
        Spacer(modifier = Modifier.width(80.dp))
        subhead2_grey(text = "Floor")
        Spacer(modifier = Modifier.weight(1f))
        subhead2_grey(
            text = "Volume"
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewCollectionsScreen() {
    ComposeAppTheme {
        InfoRow()
    }
}
