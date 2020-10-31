<template>
  <div>
    KakaoLoginPage
    <button @click="login">LOGIN</button>
  </div>
</template>

<script>
import {KAKAO, openKakaoLoginPage} from "@/service/kakao/kakao/utlls";
import axios from 'axios'

export default {
  name: "KakaoLoginPage",
  computed: {
    code() {
      return this.$route.query.code;
    },
  },
  created() {
    if (this.code) {
      const param = {
        grant_type: 'authorization_code',
        client_id: process.env.VUE_APP_KAKAO_REST_APP_KEY,
        redirect_uri: KAKAO.REDIRECT_URL,
        code: this.code,
      }
      axios.post('https://kauth.kakao.com/oauth/token', {}, {params: {...param}})
          .then(console.log);
    }
  },
  methods: {
    login() {
      openKakaoLoginPage();
    }
  }
}
</script>

<style scoped>

</style>
