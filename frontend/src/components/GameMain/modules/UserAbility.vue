<template>
  <div
    class="ability"
    v-if="abilitiesArray">
    <Abilities 
      class="badge"
      v-if="abilitiesArray"
      :ability="abilitiesArray[0]"
      :activate="!dealStateCount[abilitiesArray[0]]" />
    <Abilities
      class="badge"
      v-if="abilitiesArray"
      :ability="abilitiesArray[1]"
      :activate="!dealStateCount[abilitiesArray[1]]" />
  </div>
</template>

<script>
import Abilities from '@/components/GameMain/modules/Abilities.vue'
import {mapGetters} from 'vuex'

export default {
  components: {
    Abilities,
  },
  data() {
    return {

    }
  },
  props: {
    nickName: {
      type: String,
      default: "MagJang"
    },
	},
  methods: {
    
  },
  computed: {
    ...mapGetters([
      'dealStateCount',
      "findMyJob",
    ]),
    abilitiesArray() {
      // console.log(this.findMyJob(this.nickName))
      return this.findMyJob(this.nickName)
    }
  }
}
</script>

<style scoped>
.ability {
  display : flex;
}

</style> 