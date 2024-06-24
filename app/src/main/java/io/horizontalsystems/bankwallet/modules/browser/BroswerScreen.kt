package io.horizontalsystems.bankwallet.modules.browser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme

@Composable
fun BrowserScreen(
) {
    Column {
    //    TopBar()
        TopRow()
        HorizontalAppList()
        VerticalAppList()
    }
}


/*@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = ComposeAppTheme.colors.steelDark)
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            tint = ComposeAppTheme.colors.grey,
            contentDescription = null
        )


        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            value = "",
            onValueChange = {},
            placeholder = { Text("Search or type a URL") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    tint = ComposeAppTheme.colors.grey,
                    contentDescription = "Add"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = ComposeAppTheme.colors.jacob,
                textColor = ComposeAppTheme.colors.white,
                placeholderColor = ComposeAppTheme.colors.grey
            ),
        )
        IconButton(onClick = { *//* TODO: Handle click *//* }) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = ComposeAppTheme.colors.grey,
                contentDescription = "Add"
            )
        }
    }
}*/

@Composable
fun TopRow() {
    ScrollableTabRow(
        selectedTabIndex = 0,
        backgroundColor = ComposeAppTheme.colors.steelDark,
        contentColor = Color.White
    ) {
        BrowserTab(selected = true, onClick = { /*TODO*/ }) {
            Text("Apps", color = Color.White)
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
fun VerticalAppList() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            NetworkSample()
            Spacer(modifier = Modifier.width(8.dp))
            TypeDropDown()
        }
        featuredApps.forEach {
            TrendingAppItem(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun FeaturedAppCard(app: App) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(200.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(app.cover),
                contentDescription = app.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = app.category,
                        style = MaterialTheme.typography.body2
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
fun TrendingAppItem(app: App) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${app.position}",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.width(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = rememberAsyncImagePainter(app.icon),
            contentDescription = app.name,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = app.name,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = app.category,
                style = MaterialTheme.typography.body2
            )
        }
    }
}


data class App(
    val position: Int,
    val name: String,
    val category: String,
    val icon: String,
    val cover: String
)

val featuredApps = listOf(
    App(
        1,
        "Zeta",
        "Airdrop",
        "https://assets.coingecko.com/coins/images/37374/standard/_Z-Token-32pxpx.png?1714147975",
        "https://pbs.twimg.com/media/GQg59zEWgAAM6ea?format=jpg&name=large"
    ),
    App(
        2,
        "Raydium",
        "DeFi",
        "https://s2.coinmarketcap.com/static/img/coins/64x64/8526.png",
        "https://miro.medium.com/v2/resize:fit:1400/format:webp/0*NodmRGIxdKJYIT3Q"
    ),

    App(
        3,
        "Zeta",
        "Airdrop",
        "https://assets.coingecko.com/coins/images/37374/standard/_Z-Token-32pxpx.png?1714147975",
        "https://pbs.twimg.com/media/GQg59zEWgAAM6ea?format=jpg&name=large"
    ),
    App(
        4,
        "Raydium",
        "DeFi",
        "https://s2.coinmarketcap.com/static/img/coins/64x64/8526.png",
        "https://miro.medium.com/v2/resize:fit:1400/format:webp/0*NodmRGIxdKJYIT3Q"
    ),

    App(5, "Solana", "DeFi", "", ""),
    App(6, "Zeta", "Airdrop", "", ""),
    App(7, "Raydium", "DeFi", "", ""),
    App(8, "Solana", "DeFi", "", "")

)

@Composable
fun AppItem(name: String, category: String, imageUrl: String, bgColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .background(bgColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(name, color = Color.White, fontSize = 18.sp)
            Text(category, color = Color.Gray, fontSize = 14.sp)
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
        onClick = { /*TODO*/ }) {
        content()
    }
}


@Composable
fun NetworkSample() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.clickable {
                expanded = !expanded
            },
            verticalAlignment = Alignment.CenterVertically,) {
            Text(
                text = "All Networks",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.body2
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                content = { Text("All Networks") },
                onClick = { /* Handle edit! */ },
            )
            DropdownMenuItem(
                content = { Text("Solana") },
                onClick = { /* Handle settings! */ },
            )
            DropdownMenuItem(
                content = { Text("Ethereum") },
                onClick = { /* Handle send feedback! */ },
            )
            DropdownMenuItem(
                content = { Text("Ethereum") },
                onClick = { /* Handle send feedback! */ },
            )
        }
    }
}

@Composable
fun TypeDropDown() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.clickable {
                expanded = !expanded
            },
            verticalAlignment = Alignment.CenterVertically,) {
            Text(
                text = "Type",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.body2
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                content = { Text("Top") },
                onClick = { /* Handle edit! */ },
            )
            DropdownMenuItem(
                content = { Text("Trending") },
                onClick = { /* Handle settings! */ },
            )

        }
    }
}



@Preview(showBackground = true)
@Composable
private fun Preview_HSHodlerInput() {
    ComposeAppTheme {
        BrowserScreen()
    }
}