package io.horizontalsystems.bankwallet.modules.browser

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import coil.compose.rememberAsyncImagePainter
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.BaseComposeFragment
import io.horizontalsystems.bankwallet.core.slideFromRight
import io.horizontalsystems.bankwallet.core.stats.StatEvent
import io.horizontalsystems.bankwallet.core.stats.StatPage
import io.horizontalsystems.bankwallet.core.stats.StatSection
import io.horizontalsystems.bankwallet.core.stats.stat
import io.horizontalsystems.bankwallet.entities.browse.DApp
import io.horizontalsystems.bankwallet.entities.browse.DAppMarketing
import io.horizontalsystems.bankwallet.modules.browser.collections.Collections
import io.horizontalsystems.bankwallet.modules.browser.tokens.Tokens
import io.horizontalsystems.bankwallet.modules.coin.CoinFragment
import io.horizontalsystems.bankwallet.modules.market.SortingField
import io.horizontalsystems.bankwallet.modules.market.TopMarket
import io.horizontalsystems.bankwallet.modules.market.search.MarketSearchModule
import io.horizontalsystems.bankwallet.modules.market.search.MarketSearchScreen
import io.horizontalsystems.bankwallet.modules.market.search.MarketSearchViewModel
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.body_leah
import io.horizontalsystems.bankwallet.ui.compose.components.caption_leah
import io.horizontalsystems.bankwallet.ui.compose.components.subhead2_grey

@Composable
fun BrowserScreen(
    navController: NavController,
) {

    val browserViewModel = viewModel<BrowserViewModel>(
        factory = BrowserModule.Factory(
            TopMarket.Top100,
            SortingField.TopGainers
        )
    )



    TopRow(browserViewModel, navController)


}

@Composable
fun TopRow(
    browserViewModel: BrowserViewModel,
    navController: NavController,

    ) {

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    Column {

        //TODO USE ScrollableTabs Instead
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            backgroundColor = ComposeAppTheme.colors.transparent,
            contentColor = ComposeAppTheme.colors.tyler,
            edgePadding = 16.dp,
            indicator = @Composable { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)),
                    color = ComposeAppTheme.colors.jacob
                )
            }
        ) {
            BrowserTab(
                selected = selectedTab == 0,
                onClick = {
                    selectedTab = 0
                }) {
                Text(
                    "Apps",
                    color = if (selectedTab == 0) ComposeAppTheme.colors.leah else Color.Gray
                )
            }
            BrowserTab(
                selected = selectedTab == 1,
                onClick = {
                    selectedTab = 1
                }) {
                Text(
                    "Tokens",
                    color = if (selectedTab == 1) ComposeAppTheme.colors.leah else Color.Gray
                )
            }
            BrowserTab(
                selected = selectedTab == 2,
                onClick = {
                    selectedTab = 2
                }) {
                Text(
                    "Collections",
                    color = if (selectedTab == 2) ComposeAppTheme.colors.leah else Color.Gray
                )
            }
            BrowserTab(
                selected = selectedTab == 3,
                onClick = {
                    selectedTab = 3
                }) {
                Text(
                    "Quests",
                    color = if (selectedTab == 3) ComposeAppTheme.colors.leah else Color.Gray
                )
            }
            BrowserTab(
                selected = selectedTab == 4,
                onClick = {
                    selectedTab = 4
                }) {
                Text(
                    "Learn",
                    color = if (selectedTab == 4) ComposeAppTheme.colors.leah else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        if (selectedTab == 0) {
            browserViewModel.dAppsResponse.collectAsState().value?.ads?.let {
                HorizontalAppList(
                    it.marketing
                )
            }
            VerticalAppListParent(browserViewModel)
        } else if (selectedTab == 1) {
            Tokens(onCoinClick = { onCoinClick(it, navController) })
        } else if (selectedTab == 2) {
            Collections()
        }

    }

}


@Composable
fun HorizontalAppList(
    featuredApps: List<DAppMarketing>?
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        featuredApps?.forEach {
            FeaturedAppCard(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun VerticalAppList(trendApps: List<DApp>?) {
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
            TrendingAppItem(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun VerticalAppListParent(
    browserViewModel: BrowserViewModel
) {
    var selectedNetworkType by remember { mutableStateOf(NetworkType.ALL_NETWORKS) }
    var selectedNetworkState by remember { mutableStateOf(NetworkState.TREND) }

    val filteredApps =
        when {
            selectedNetworkState == NetworkState.TREND && selectedNetworkType == NetworkType.ALL_NETWORKS ->
                browserViewModel.dAppsResponse.collectAsState().value?.trend?.all

            selectedNetworkState == NetworkState.TREND && selectedNetworkType == NetworkType.SOLANA ->
                browserViewModel.dAppsResponse.collectAsState().value?.trend?.solana

            selectedNetworkState == NetworkState.TREND && selectedNetworkType == NetworkType.ETHEREUM ->
                browserViewModel.dAppsResponse.collectAsState().value?.trend?.ethereum

            selectedNetworkState == NetworkState.TREND && selectedNetworkType == NetworkType.POLYGON ->
                browserViewModel.dAppsResponse.collectAsState().value?.trend?.polygon

            selectedNetworkState == NetworkState.TOP && selectedNetworkType == NetworkType.ALL_NETWORKS ->
                browserViewModel.dAppsResponse.collectAsState().value?.top?.all

            selectedNetworkState == NetworkState.TOP && selectedNetworkType == NetworkType.SOLANA ->
                browserViewModel.dAppsResponse.collectAsState().value?.top?.solana

            selectedNetworkState == NetworkState.TOP && selectedNetworkType == NetworkType.ETHEREUM ->
                browserViewModel.dAppsResponse.collectAsState().value?.top?.ethereum

            else -> browserViewModel.dAppsResponse.collectAsState().value?.top?.polygon

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

        VerticalAppList(filteredApps)
    }
}


@Composable
fun FeaturedAppCard(dAppMarketing: DAppMarketing) {
    val viewModel = LocalViewModel.current
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(200.dp)
            .clickable {
                viewModel.onGo(dAppMarketing.url, context = context)
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
                painter = rememberAsyncImagePainter(dAppMarketing.cover),
                contentDescription = dAppMarketing.name,
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
                        text = dAppMarketing.name
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    caption_leah(
                        text = dAppMarketing.category[0]
                    )
                }

                Image(
                    painter = rememberAsyncImagePainter(dAppMarketing.icon),
                    contentDescription = dAppMarketing.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                )


            }

        }


    }
}

@Composable
fun TrendingAppItem(dApp: DApp) {
    val viewModel = LocalViewModel.current
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                viewModel.onGo(dApp.url, context)
            },

        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${dApp.id}",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.width(24.dp),
            color = ComposeAppTheme.colors.lightGrey
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = rememberAsyncImagePainter(dApp.icon),
            contentDescription = dApp.name,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            body_leah(
                text = dApp.name,
            )
            subhead2_grey(
                text = dApp.category[0],
            )
        }
    }
}


@Composable
fun BrowserTab(
    selected: Boolean,
    selectedContentColor: Color = LocalContentColor.current,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit


) {
    Tab(
        modifier = Modifier.padding(vertical = 16.dp),
        selected = selected,
        onClick = { onClick() })
    {
        content()
    }
}


@Composable
fun NetworkSample(
    selectedNetworkType: NetworkType,
    onNetworkTypeSelected: (NetworkType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .background(ComposeAppTheme.colors.lawrence),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            body_leah(
                text = selectedNetworkType.displayName,
                modifier = Modifier.padding(8.dp),
            )
            Icon(
                painter = painterResource(R.drawable.ic_down_arrow_20),
                contentDescription = null,
                tint = Color.Unspecified

            )
        }
        DropdownMenu(
            modifier = Modifier.background(ComposeAppTheme.colors.lawrence),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                content = { body_leah("Solana") },
                onClick = {
                    expanded = false
                    onNetworkTypeSelected(NetworkType.SOLANA)

                }
            )
            DropdownMenuItem(
                content = { body_leah("Ethereum") },
                onClick = {
                    expanded = false
                    onNetworkTypeSelected(NetworkType.ETHEREUM)
                },
            )
            DropdownMenuItem(
                content = { body_leah("Polygon") },
                onClick = {
                    expanded = false
                    onNetworkTypeSelected(NetworkType.POLYGON)
                },
            )
            DropdownMenuItem(
                content = { body_leah("All Networks") },
                onClick = {
                    expanded = false
                    onNetworkTypeSelected(NetworkType.ALL_NETWORKS)
                },
            )
        }


    }
}

@Composable
fun TypeDropDown(
    selectedNetworkState: NetworkState,
    onNetworkStateSelected: (NetworkState) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .background(ComposeAppTheme.colors.lawrence),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DropdownMenu(
                modifier = Modifier.background(ComposeAppTheme.colors.lawrence),
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    content = { body_leah("Trending") },
                    onClick = {
                        expanded = false
                        onNetworkStateSelected(NetworkState.TREND)
                    },
                )
                DropdownMenuItem(
                    content = { body_leah("Top") },
                    onClick = {
                        expanded = false
                        onNetworkStateSelected(NetworkState.TOP)
                    },
                )


            }
            body_leah(
                text = selectedNetworkState.stateName,
                modifier = Modifier.padding(8.dp),
            )
            Icon(
                painter = painterResource(R.drawable.ic_down_arrow_20),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

    }
}

private fun onCoinClick(coinUid: String, navController: NavController) {
    val arguments = CoinFragment.Input(coinUid)

    try {
        navController.slideFromRight(R.id.coinFragment, arguments)
        stat(
            page = StatPage.Browse,
            section = StatSection.Tokens,
            event = StatEvent.OpenCoin(coinUid)
        )
    } catch (e: IllegalStateException) {
        Log.e("NavigationError", "Navigation failed: ${e.message}")
    }
}

