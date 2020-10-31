package me.sun.notificationservice.domain.sender.model

interface KakaoMsg

data class KakaoMsgListType(
        val objectType: String = "list",
        val headerTitle: String,
        val headerLink: KakaoMsgLink = KakaoMsgLink.EMPTY,
        val contents: List<KakaoMsgContent>
) : KakaoMsg

data class KakaoMsgLink(
        val webUrl: String = ""
) {
    companion object {
        val EMPTY = KakaoMsgLink()
    }
}

data class KakaoMsgContent(
        val title: String,
        val imageUrl: String = "",
        val link: KakaoMsgLink = KakaoMsgLink.EMPTY
)
