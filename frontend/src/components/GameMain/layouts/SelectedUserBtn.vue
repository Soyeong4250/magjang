<template>
  <div v-if="voter">
    <div class="deal-text">
      <span style="font-size:20px; font-weight: 700;">🤝거래를<br />체결하시겠습니까?</span> <br />
      <span>* 혼자 배신한다면 이득을 얻을 수 있지만, 2명이상 배신할 경우 패널티가 부여됩니다.</span>
    </div>
    <div class="btns">
      <button
        type="button"
        class="btn btn-ok btn-success"
        @click="voteOk">
        체결
      </button>
      <button
        type="button"
        class="btn btn-nope btn-danger"
        @click="voteNo">
        미체결
      </button>
    </div>
  </div>
  <div v-else> 
    <div class="deal-text">
      <span>*거래가 진행중입니다. 잠시만 기다려주세요.</span> <br />
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  data() {
    return {
      // 자신이 투표자인지 알려주는 boolean
      // isVoter: this.$store.getters.voter,
    };
  },
  methods: {
    ...mapActions([
      "setVoter",
      "setBroker",
    ]),
    voteOk(){
      console.log("체결 찬성")
      this.emitter.emit('vote', true);
      this.setVoter(false)
      // this.setBroker(false)
    },
    voteNo(){
      console.log("체결 반대")
      this.emitter.emit('vote', false);
      this.setVoter(false)
      // this.setBroker(false)
    },
  },
  computed: {
    ...mapGetters([
      "voter",
    ])
  },
}
</script>

<style scoped>
.deal-text {
  font-size: 16px;
}

.btns {
  margin: 10px;
  display: flex;
  justify-content: space-evenly;
}
</style>