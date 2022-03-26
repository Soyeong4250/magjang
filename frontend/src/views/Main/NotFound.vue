<template>
  <div class="notfound">
    <div class="error">
      <img
        @click="goHome"
        src="@/assets/logo.gif"
        alt="logo" />
    </div>
    <div class="title">
      <span class="jang">404 <br />Not Found</span>
    </div>
    <button
      class="btn mj-btn"
      type="info"
      @click="goHome">
      메인으로 돌아가기
    </button>
  </div>
</template>

<script>
import { useRouter } from "vue-router";

export default {
  name: "NotFound",
  setup() {
    const router = useRouter();
    const goHome = () => {
      router.push("/");
    };
    return {
      goHome,
    };
  },
};
</script>

<style>
  .notfound .error {
    text-align: center;
    width: 400px;
    height: 400px;
    transition: all 0.2s linear;
    cursor: pointer;
    display: block;
    margin: auto;
  }

  .notfound .error img {
    width: 100%;
    height: 100%;
  }

  .notfound .logo img:hover {
    transform: scale(1.05);
  }

  .title {
    text-align: center;
    font-weight: 700;
    font-size: 70px;
    color: #fff;
  }
  .title .jang {
    font-size: 90px;
    color: #F7CE0A
  }
</style>