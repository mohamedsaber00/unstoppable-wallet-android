package io.horizontalsystems.bankwallet.entities.browse


data class CollectionsResponse(
    val trend: CollectionsTrend,
    val top: CollectionsTop,
    val ads: CollectionsAds
)

data class CollectionsNFT(
    val id: Int,
    val name: String,
    val url: String,
    val icon: String,
    val network: List<String>,
    val floor: String,
    val volume: String,
    val volume_change: Double
)

data class CollectionsTrend(
    val solana: List<CollectionsNFT>,
    val ethereum: List<CollectionsNFT>,
    val polygon: List<CollectionsNFT>,
    val all: List<CollectionsNFT>
)

data class CollectionsTop(
    val solana: List<CollectionsNFT>,
    val ethereum: List<CollectionsNFT>,
    val polygon: List<CollectionsNFT>,
    val all: List<CollectionsNFT>
)


data class CollectionsDefi(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class CollectionsExchange(
    val id: Int,
    val name: String,
    val category: List<String>,
    val url: String,
    val icon: String,
    val cover: String,
    val network: List<String>
)

data class CollectionsMarketing(
    val id: Int,
    val name: String,
    val url: String,
    val icon: String,
    val network: List<String>,
    val floor: String,
    val volume: String,
    val volumeChange: Double
)

data class CollectionsAds(
    val marketing: List<CollectionsMarketing>
)

data class CollectionsCryptoProjects(
    val defi: List<CollectionsDefi>,
    val exchanges: List<CollectionsExchange>,
    val collectionsAds: CollectionsAds
)