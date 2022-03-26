<template>
  <div v-if="nickName">
    <GameRoom />
  </div>
  <div v-else>
    <CreateNickname />
  </div>
  <!-- 로그아웃필요 -->
</template>

<script>
import CreateNickname from '@/components/Main/layouts/CreateNickname.vue'
import GameRoom from '@/components/Main/layouts/GameRoom.vue'
import {mapGetters} from 'vuex'

export default {
  components: {
    CreateNickname,
    GameRoom,
  },

  computed: {
    ...mapGetters([
      'nickName',
    ])
  },

}
</script>

<style scoped>

</style>