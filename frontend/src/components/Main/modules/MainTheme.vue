<template>
  <div class="main">
    <div class="logo">
      <img
        @click="home"
        src="@/assets/logo.gif"
        alt="logo" />
    </div>
    <div class="title">
      <span class="mag">막?</span>      
      <span>나가는</span>
      <span class="jang">장!</span>
      <span>사꾼</span>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    home: function() {
      this.$router.push({ name: 'Home' })
    }
  }
}
</script>

<style scoped>
  .main .logo {
    text-align: center;
  }
  .main .logo img {
    text-align: center;
    width: 200px;
    height: 200px;
    transition: all 0.2s linear;
    cursor: pointer;
  }
  .main .logo img:hover {
    transform: scale(1.05);
  }
  .main .title {
    text-align: center;
    font-weight: 700;
    font-size: 70px;
    color: #fff;
  }
  .main .title .mag {
    font-size: 90px;
    color: #720000;
  }
  .main .title .jang {
    font-size: 90px;
    color: #F7CE0A
  }
</style>