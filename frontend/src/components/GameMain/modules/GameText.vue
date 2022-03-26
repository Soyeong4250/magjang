<template>
  <div class="game_text">
    <div
      v-if="broker"> 
      <span>플레이어 화면을 <br /> 클릭하면, <br /> 금액창이 나타납니다.<br /></span>
      <span>조건에 맞춰 금액을 <br /> 배분해주세요!</span>
      <!-- 게임 인원수, 능력, 돈 체크 해서 결정완료 버튼 활성화  -->
      <!-- 동시에 타이머 스킵 -->
      <!-- 결정완료를 누르면 메소드가 실행되어서 전체 인원에게 입력한 금액 보여줌 -->
      <!-- 그리고 해당되는 사람들에게만 체결하기/거부하기 띄어주기 -->
      <!-- v-if="game_condition_player == click_player" -->
      <button
        v-if="dealStateCount"
        type="button"
        class="btn mj-btn"
        :class="{disabled: !isDealPossible}"
        @click="sendConclusion">
        결정완료
      </button>
    </div>
    <div v-else> 
      <span>브로커가 거래를 <br />체결 중 입니다. </span><br />
      <span>잠시만 기다려주세요.</span>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  data () {
    return {
      
    }
  },
  methods: {
    sendConclusion () {
      this.setConclusion(false);
      this.setIsDealClicked(true);
      // this.setBroker(false);
      this.emitter.emit('sendConclusion', true);//거래를 성공적으로 제안했는지 : true
      
    },
    ...mapActions([
      "setConclusion",
      "setIsDealClicked",
      "setBroker",
    ])
  },
  computed: {
    ...mapGetters([
      'isDealPossible',
      "nickName",
      "broker",
      "conclusion",
      "dealStateCount",
    ])
  },
}
</script>

<style scoped>
.game_text {
  color: black;
  font-size: 18px;
  position: absolute;
  top: 67%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
}

.game_text .btn {
  width: 100px;
  /*color: #198754;*/
  border: 2px solid #198754;
  background-color: #198754;
  color: #fff;
  margin-top: 20px;
}
</style>