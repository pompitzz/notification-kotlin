const routes = [
    {
        path: '/',
        redirect: '/kakao-login',
    },
    {
        path: '/kakao-login',
        name: 'KakaoLoginPage',
        component: () => import('../view/KakaoLoginPage.vue'),
    },
];

export default routes;
