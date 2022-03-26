<template>
  <div id="main" style="overflow: hidden">
    <div class="container">
      <MainTheme />
      <MainText />
      <IsLogined />
    </div>
  </div>
</template>

<script>
import MainTheme from "@/components/Main/modules/MainTheme.vue";
import MainText from "@/components/Main/modules/MainText.vue";
import IsLogined from "@/components/Main/layouts/IsLogined.vue";

export default {
  components: {
    MainTheme,
    MainText,
    IsLogined,
  },
};
</script>

<style>
#main {
  height: 100vh;
  width: 100%;
  background-image: url("~@/assets/money_bg1.jpg");
  background-repeat: no-repeat;
  /* background-attachment: fixed; */
  background-size: cover;
  background-blend-mode: overlay;
  background-color: rgba(0, 0, 0, 0.5);
}
</style>
