import Kakao from './sdk.js';

function openKakaoLoginPage() {
    if (!Kakao.isInitialized()) {
        Kakao.init(process.env.VUE_APP_KAKAO_JS_APP_KEY);
    }
    Kakao.Auth.authorize({
        redirectUri: KAKAO.REDIRECT_URL,
    });
}

const KAKAO = {
    REDIRECT_URL: `${window.location.protocol}//${window.location.host}/kakao-login`,
};

export {
    openKakaoLoginPage,
    KAKAO,
};
