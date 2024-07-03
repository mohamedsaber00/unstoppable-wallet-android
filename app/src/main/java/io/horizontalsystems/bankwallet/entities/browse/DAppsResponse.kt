package io.horizontalsystems.bankwallet.entities.browse


data class DAppResponse(
    val trend: Trend,
    val top: Top,
    val ads: Ads
)

data class DApp(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class Trend(
    val solana: List<DApp>,
    val ethereum: List<DApp>,
    val polygon: List<DApp>,
    val all: List<DApp>
)

data class Top(
    val solana: List<DApp>,
    val ethereum: List<DApp>,
    val polygon: List<DApp>,
    val all: List<DApp>
)


data class Defi(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class Exchange(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class Marketing(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class Ads(
    val marketing: List<Marketing>
)

data class CryptoProjects(
    val defi: List<Defi>,
    val exchanges: List<Exchange>,
    val ads: Ads
)