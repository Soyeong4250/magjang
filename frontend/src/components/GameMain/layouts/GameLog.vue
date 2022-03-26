<template>
  <div
    class="game-play-log"
    ref="proList">
    <span class="game-log-title"> 📢게임로그 </span>
    <div
      class="log-item"
      v-for="(item, idx) in proList"
      :key="idx">
      {{ item }}
    </div>      
  </div>
</template>

<script>
export default {
  data() {
    return {
      proList: []
    }
  },
  updated() {
    // 새로운 채팅이 입력되면 스크롤 하단으로 update
    let objDiv = this.$refs.proList;
    objDiv.scrollTo({ top: objDiv.scrollHeight, behavior:'smooth'});
  },
  mounted() {
    // 게임 시작하자마자 보내는 로그
    // '게임이 시작되었습니다 :) 1등을 향해'
    this.emitter.on('logGameStart', msg => this.pushup(msg))

    // 능력이 분배되었다고 보내는 로그 
    // "플레이어들의 능력을 분배하겠습니다!"
    this.emitter.on('logAbilityShow', msg => this.pushup(msg))

    // 전체 브로커 제시하는 로그
    // 'i+'번째 브로커는'+order[i]+'님입니다.'
    // 1번째 브로커는 '가은'님입니다.
    // 2번째 브로커는 '채은'님 입니다.
    this.emitter.on('logRoundBrokerOrder', msg => this.pushup(msg))

    // 해당 턴의 브로커 제시하는 로그
    // 이번 턴의 브로커는 '가은'님 입니다.
    this.emitter.on('logRoundBroker', msg => this.pushup(msg))

    //  브로커가 선택한 플레이어
    //  '이번 거래는 '+ this.playerResult +' 가 참여합니다.'
    this.emitter.on('logDealState', msg => this.pushup(msg))

    // 브로커가 선택한 플레이어의 투표 현황
    // '채은'님께서 거래 성사 여부를 결정했습니다.
    this.emitter.on('logVoteState', msg => this.pushup(msg))

    // 거래 체결 여부 반환
    // '이번 거래는 '+ this.playerResult +'되었습니다.'
    // 이번 거래는 체결되었습니다, 결렬되었습니다.
    this.emitter.on('logFinalVote', msg => this.pushup(msg))

    // 라운드 순위 반환
    // 현재 가장 갑부는 '가은'입니다.
    this.emitter.on('logRoundWin', msg => this.pushup(msg))

    // 나머지 사람들 순위 반환
    // 현재 2번째 갑부는 '채은' 입니다.
    this.emitter.on('logRoundRank', msg => this.pushup(msg))
    
    

  },
  methods:{
    pushup(msg){
      this.proList.push(msg)
    }
  },
}
</script>

<style scoped>
/* height 45로 바꾸기 */
.game-play-log {
  font-size: 16px;
  width: 100%;
  height: 45vh;
  overflow-y: scroll;
  -ms-overflow-style: none;
  margin-bottom: 10px;
  padding: 10px;
  border: solid rgb(67,66,47);
  border-radius: 10px;
}

.game-play-log::-webkit-scrollbar {
  display: none;
}

.game-log-title {
  font-size: 30px;
  display: block;
}

.log-item {
  display: inline-block;
  margin-top: 10px;
}

</style>