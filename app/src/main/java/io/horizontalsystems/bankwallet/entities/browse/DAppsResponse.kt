package io.horizontalsystems.bankwallet.entities.browse


data class DAppResponse(
    val trend: DAppTrend,
    val top: DAppTop,
    val ads: DAppAds
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

data class DAppTrend(
    val solana: List<DApp>,
    val ethereum: List<DApp>,
    val polygon: List<DApp>,
    val all: List<DApp>
)

data class DAppTop(
    val solana: List<DApp>,
    val ethereum: List<DApp>,
    val polygon: List<DApp>,
    val all: List<DApp>
)


data class DAppDefi(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class DAppExchange(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class DAppMarketing(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class DAppAds(
    val marketing: List<DAppMarketing>
)

data class DAppCryptoProjects(
    val dAppDefis: List<DAppDefi>,
    val dAppExchanges: List<DAppExchange>,
    val DAppAds: DAppAds
)