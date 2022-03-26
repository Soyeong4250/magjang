<template>
  <div>
    <div
      class="col"
      v-if="streamManager"
      :key="streamManager.stream.connection.connectionId">
      <UserVideo
        :stream-manager="streamManager"
        :abilities-array="abilitiesArray"
        :index="index"
        :nick-name="nickName"
        @click="selectPriceShow" />
    </div>
    <div
      v-if="player && broker && gamePossible">
      <TotalPrice />
    </div>
    <div
      v-else-if="selectedUser && broker && gamePossible">
      <SelectPrice 
        :selected-user="selectedUser"
        :player="player" 
        :index="index" />
    </div>
  </div>
</template>

<script>
import UserVideo from '@/components/GameMain/modules/UserVideo.vue'
import TotalPrice from '@/components/GameMain/modules/TotalPrice.vue'
import SelectPrice from '@/components/GameMain/modules/SelectPrice.vue'
import { mapActions, mapGetters } from 'vuex'

export default {
  components: { 
    UserVideo,
    TotalPrice,
    SelectPrice,
  },
  data() {  
    return{
      selectedUser: false,
    }
  },
  props: {
		streamManager: {
      type: Object,
      default: null,
    },
    player: {
      type: Boolean,
      default: false
    },
    index: {
      type: Number,
      default: -1
    }
	},
  methods: {
    ...mapActions([
      "userSelectEvent",
      "setUserNickName",
    ]),
    getConnectionData () {
      setTimeout(() => {
        
      }, 1);
			const { connection } = this.streamManager.stream;
			return JSON.parse(connection.data);
		},
    selectPriceShow(){
      if (!this.player && this.broker && (this.dealPrice >= 200 || this.selectedUser)) {
        this.selectUser();
      }
      else {
        console.log(this.player, this.broker, this.dealPrice, this.selectedUser)
      }
    },
    selectUser() {
      // console.log("selectUser")
      this.selectedUser = !this.selectedUser;
        this.userSelectEvent({
          "first_ability": this.abilitiesArray[0],
          "second_ability": this.abilitiesArray[1], 
          "isUserSelected": this.selectedUser
        })
    },
    getAbilities(jobsList) {
      console.log(this.$store.getters.nickName)
      console.log(jobsList)
      // const abilityList = ['창고','인맥','언변','정보','로비','선박',]
      // this.abilitiesArray = _.sampleSize(abilityList, 2)

    },
  },
  mounted() {
  //  this.emitter.on('playerAbility', playerAbilities => this.getAbilities(playerAbilities))
    // this.emitter.off('initJobs')
    this.emitter.on('initJobs', jobsList => this.getAbilities(jobsList))
  },
  computed: {
    ...mapGetters([
      "broker",
      "gamePossible",
      "dealPrice",
      "findMyJob",
      "turnPrice",
    ]),
    nickName () {
			const { clientData } = this.getConnectionData();
      this.setUserNickName({"NickName": clientData, "index": this.index})
			return clientData;
		},
    abilitiesArray() {
      return this.findMyJob(this.nickName)
    },
  },
  watch: {
    broker(nowValue) {
      console.log("broker 변화 탐지!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:", nowValue)
      if (!nowValue){
        // this.selectUser();
        this.selectedUser = false
        console.log("브로커 해제, 자신 선택 해제")
      }
    },
    turnPrice(nowValue) {
      // console.log("broker is true")
      if (this.player && this.broker) {
        nowValue;
        this.selectUser();
        console.log("브로커 배당, 자신 선택")
      }
    }
  }
}
</script>

<style scoped>
/* .col {
  background: rgb(156, 158, 127);
} */
</style>