package me.sun.notificationservice.common

object URL {
    const val CORONA_STATUS = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13&ncvContSeq=&contSeq=&board_id=&gubun="
    const val KAKAO_MESSAGE = "https://kapi.kakao.com/v2/api/talk/memo/default/send"
    const val KAKAO_PROFILE = "https://kapi.kakao.com/v2/user/me"
    const val KAKAO_REFRESH_TOKEN = "https://kauth.kakao.com/oauth/token"
}

// TODO 저장소 공개 시 기존 토큰 앱은 제거 후 새로 만들자
object TOKEN {
    const val KAKAO_REST_API = "ae23bcc0eb0a5bfe0eb07767dc019cd0"
}
