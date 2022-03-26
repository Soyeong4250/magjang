<template>
  <div
    class="text-center"
    id="naver_id_login">
  </div>
</template>

<script>
export default {
  mounted() {
    const naver_id_login = new window.naver_id_login("XcnVRW5TXoLW3FDEkiUJ", "https://i6b208.p.ssafy.io/navercallback");
    const state = naver_id_login.getUniqState();
    naver_id_login.setButton("green", 3, 80)
    naver_id_login.setState(state);
    // naver_id_login.setPopup();
    naver_id_login.init_naver_id_login();
  }
}
</script>

<style scoped>
  #naver_id_login:hover {
    transform: scale(1.05);
  }
</style>