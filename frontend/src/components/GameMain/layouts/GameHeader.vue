<template>
  <div class="nav-top">
    <GameRule />
    <div v-if="gamePossible">
      <UserPrice />
    </div>
  </div>
</template>
<script>
// import InputPrice from '@/components/GameMain/modules/InputPrice.vue'
import GameRule from '@/components/GameMain/layouts/GameRule.vue'
import UserPrice from '@/components/GameMain/modules/UserPrice.vue'
import { mapGetters, mapActions } from 'vuex'

export default {
  components: {
    GameRule,
    UserPrice,
  },
  computed: {
    ...mapGetters([
      "gamePossible",
      "broker",
    ])
  },
  methods: {
    ...mapActions([
      "setBroker",
    ]),
    setBrokers() {
      this.setBroker(!this.broker);
    }
  }
}
</script>

<style>

</style>