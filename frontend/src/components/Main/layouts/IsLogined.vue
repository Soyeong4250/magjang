<template>
  <div v-if="isLogined">
    <IsNickname />
  </div>
  <div v-else>
    <NaverLogInBtn />
  </div>
  <!-- <testButton /> -->
</template>

<script>
import {mapGetters} from 'vuex'
import NaverLogInBtn from '@/components/Main/modules/NaverLogInBtn.vue'
import IsNickname from '@/components/Main/layouts/IsNickname.vue'
// import testButton from '@/components/Main/modules/testButton.vue'

export default {
  components: {
    NaverLogInBtn,
    IsNickname,
    // testButton,
  },
  computed: {
    ...mapGetters([
      'isLogined',
    ])
  }
}
</script>

<style scoped>

</style>