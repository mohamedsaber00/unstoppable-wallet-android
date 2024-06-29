package io.horizontalsystems.bankwallet.modules.browser

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
import androidx.compose.runtime.getValue
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
import coil.compose.rememberAsyncImagePainter
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.body_leah
import io.horizontalsystems.bankwallet.ui.compose.components.caption_leah
import io.horizontalsystems.bankwallet.ui.compose.components.subhead2_grey

@Composable
fun BrowserScreen(
) {
    Column(
    )
    {
        //    TopBar()
        TopRow()
        HorizontalAppList()
        VerticalAppListParent()
    }
}

@Composable
fun TopRow() {
    //TODO USE ScrollableTabs Instead
    ScrollableTabRow(
        selectedTabIndex = 0,
        backgroundColor = ComposeAppTheme.colors.transparent,
        contentColor = ComposeAppTheme.colors.tyler,
        edgePadding = 16.dp,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[0])
                    .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)),
                color = ComposeAppTheme.colors.jacob
            )
        }
    ) {
        BrowserTab(selected = true, onClick = { /*TODO*/ }) {
            Text("Apps", color = ComposeAppTheme.colors.leah)
        }
        BrowserTab(selected = false, onClick = { /*TODO*/ }) {
            Text("Tokens", color = Color.Gray)
        }
        BrowserTab(selected = false, onClick = { /*TODO*/ }) {
            Text("Collections", color = Color.Gray)
        }
        BrowserTab(selected = false, onClick = { /*TODO*/ }) {
            Text("Quests", color = Color.Gray)
        }
        BrowserTab(selected = false, onClick = { /*TODO*/ }) {
            Text("Learn", color = Color.Gray)
        }
    }
}

@Composable
fun HorizontalAppList() {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        featuredApps.forEach {
            FeaturedAppCard(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun VerticalAppList(filteredApps: List<TrendingApp>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Spacer(modifier = Modifier.width(8.dp))
        }

        filteredApps.forEach {
            TrendingAppItem(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun VerticalAppListParent() {
    var selectedNetworkType by remember { mutableStateOf(NetworkType.ALL_NETWORKS) }

    val filteredApps = if (selectedNetworkType == NetworkType.ALL_NETWORKS) {
        trendingApps
    } else {
        trendingApps.filter { it.networkType.contains(selectedNetworkType) }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            TypeDropDown()
            Spacer(modifier = Modifier.width(8.dp))
            NetworkSample(selectedNetworkType) { newNetworkType ->
                selectedNetworkType = newNetworkType
            }
        }

        VerticalAppList(filteredApps)
    }
}


@Composable
fun FeaturedAppCard(app: App) {
    val viewModel = LocalViewModel.current
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(200.dp)
            .clickable {
                viewModel.onGo(app.url, context = context)
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
                painter = rememberAsyncImagePainter(app.cover),
                contentDescription = app.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)) {
                Column(Modifier.weight(1f)) {
                    body_leah(
                        text = app.name
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    caption_leah(
                        text = app.category
                    )
                }

                Image(
                    painter = rememberAsyncImagePainter(app.icon),
                    contentDescription = app.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                )


            }

        }


    }
}

@Composable
fun TrendingAppItem(trendingApp: TrendingApp) {
    val viewModel = LocalViewModel.current
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                viewModel.onGo(trendingApp.url, context)
            },

        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${trendingApp.position}",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.width(24.dp),
            color = ComposeAppTheme.colors.lightGrey
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = rememberAsyncImagePainter(trendingApp.icon),
            contentDescription = trendingApp.name,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            body_leah(
                text = trendingApp.name,
            )
            subhead2_grey(
                text = trendingApp.category,
            )
        }
    }
}

data class App(
    val position: Int,
    val name: String,
    val category: String,
    val icon: String,
    val cover: String,
    val url: String
)

val featuredApps = listOf(
    App(
        1,
        "Helium",
        "Depin",
        "https://assets.coingecko.com/coins/images/4284/standard/Helium_HNT.png?1696504894",
        "",
        "https://www.helium.com/"
    ),
    App(
        2,
        "Gamma",
        "Marketplace",
        "https://mma.prnewswire.com/media/1805495/GAMMA_Logo.jpg?w=200",
        "",
        "https://stacks.gamma.io/"
    ),
    App(
        3,
        "Rain.fi",
        "DeFi",
        "https://rain.fi/RainLogoMobile.svg",
        "",
        "https://rain.fi/"
    ),

    App(
        4,
        "Atlas3",
        "Community",
        "https://atlas3.io/images/logo.svg",
        "",
        "https://atlas3.io/"
    ),

    App(
        5,
        "Pooper",
        "Tools",
        "https://www.pooperscooper.app/images/scooper_logo.png",
        "",
        "https://www.pooperscooper.app/"
    ),
    App(
        6,
        "Zealy",
        "Community",
        "https://assets.bitdegree.org/images/best-crypto-quest-platforms-zealy-small.png?tr=w-200",
        "",
        "https://zealy.io/"
    ),
    App(
        7,
        "Zora",
        "Art",
        "https://assets.coingecko.com/markets/images/1479/large/zora.jpeg?1709872000",
        "",
        "https://zora.co/?feed=following"
    ),
    App(
        8,
        "Sunflower",
        "Gaming",
        "https://assets.coingecko.com/coins/images/25514/standard/download.png?1696524647",
        "",
        "https://sunflower-land.com/"
    ),
    App(
        9,
        "OpenSea",
        "DeFi",
        "https://opensea.io/static/images/logos/opensea-logo.svg",
        "",
        "https://opensea.io/"
    )
)


data class TrendingApp(
    val position: Int,
    val name: String,
    val category: String,
    val icon: String,
    val cover: String,
    val networkType: List<NetworkType>,
    val url: String
)

var trendingApps = listOf(
    TrendingApp(
        1,
        "Jupiter",
        "DeFi",
        "https://assets.coingecko.com/coins/images/34188/standard/jup.png?1704266489",
        "https://pbs.twimg.com/media/GQg59zEWgAAM6ea?format=jpg&name=large",
        listOf(NetworkType.SOLANA),
        "https://jup.ag/"
    ),
    TrendingApp(
        2,
        "Magic Eden",
        "Marketplace",
        "https://assets.coingecko.com/markets/images/1609/large/magiceden.png?1716884981",
        "",
        listOf(NetworkType.SOLANA, NetworkType.ETHEREUM, NetworkType.POLYGON),
        "https://magiceden.io/"

    ),
    TrendingApp(
        3,
        "Raydium",
        "DeFi",
        "https://s2.coinmarketcap.com/static/img/coins/64x64/8526.png",
        "",
        listOf(NetworkType.SOLANA),
        "https://raydium.io/swap/"

    ),

    TrendingApp(
        4,
        "Sanctum",
        "Staking",
        "https://assets.coingecko.com/coins/images/38485/standard/IAcXR9Dr_400x400.jpg?1717679016",
        "",
        listOf(NetworkType.SOLANA),
        "https://www.sanctum.so/"

    ),

    TrendingApp(
        5,
        "pump.fun",
        "DeFi",
        "https://pump.fun/_next/image?url=%2Flogo.png&w=32&q=75",
        "",
        listOf(NetworkType.SOLANA),
        "https://pump.fun/board"

    ),
    TrendingApp(
        6,
        "DRiP",
        "Collectibles",
        "https://drip.haus/drip_logo_white.a87ccb99.svg",
        "",
        listOf(NetworkType.SOLANA),
        "https://pump.fun/board"
    ),
    TrendingApp(
        7,
        "Sunflower Land",
        "Gaming",
        "https://assets.coingecko.com/coins/images/25514/standard/download.png?1696524647",
        "",
        listOf(NetworkType.POLYGON),
        "https://sunflower-land.com/"
    ),
    TrendingApp(
        8,
        "DEX Screener",
        "DeFi",
        "https://dexscreener.com/favicon.ico",
        "",
        listOf(NetworkType.SOLANA),
        "https://dexscreener.com/"
    ),
    TrendingApp(
        9,
        "OnePlanet",
        "Gaming",
        "https://img.cryptorank.io/coins/one_planet1668760388627.png",
        "",
        listOf(NetworkType.POLYGON),
        "https://www.oneplanetnft.io/"

    ),
    TrendingApp(
        10,
        "OpenSea",
        "DeFi",
        "https://opensea.io/static/images/logos/opensea-logo.svg",
        "",
        listOf(NetworkType.ETHEREUM, NetworkType.POLYGON),
        "https://opensea.io/"
    )

)


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
        onClick = { /*TODO*/ }) {
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
fun TypeDropDown() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember {
        mutableStateOf("Trending")
    }

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
                    content = { body_leah("Top") },
                    onClick = {
                        expanded = false
                        selectedOption = "Top"
                    },
                )
                DropdownMenuItem(
                    content = { body_leah("Trending") },
                    onClick = {
                        expanded = false
                        selectedOption = "Trending"
                    },
                )

            }
            body_leah(
                text = selectedOption,
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


@Preview(showBackground = true)
@Composable
private fun PreviewBrowserScreen() {
    ComposeAppTheme {
        BrowserScreen()
    }
}