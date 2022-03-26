<template>
  <div>
    <h1 class="winner-text">
      🏆 {{ winner }} 님이 우승하셨습니다.
    </h1>

    <!-- 게임 전체 로그 모달 누르는 버튼 -->

    <button
      type="button"
      class="btn mj-btn rule-info"
      style="margin-right: 10px;"
      data-bs-toggle="modal"
      data-bs-target="#GameLogModal">
      게임로그
    </button>

    <!-- 게임 전체 로그 모달 -->
    <div
      class="modal fade"
      id="GameLogModal"
      role="dialog"
      tabindex="-1"
      aria-labelledby="GameLogModal
      Label"
      aria-hidden="true">
      <div
        class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
        <div
          class="modal-content">
          <div class="modal-header">
            <h5
              class="modal-title"
              id="GameLogModal
              Label">
              🎲 전체 게임 결과 발표 🎲
            </h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close">
            </button>
          </div>
          <!-- 모달 보여지는 테이블 -->
          <div class="modal-body">
            <!-- 열: 인원수 * 라운드 -->
            
            <table class="table">
              <thead>
                <tr>
                  <th scope="col">
                    게임 로그
                  </th>
                  <th 
                    scope="col"
                    v-for="(playerNick, i) in playerList"
                    :key="i">
                    {{ playerNick }}
                  </th>
                </tr>
              </thead>
              <tbody>
                <!--1 Round 구분행 생성-->
                <tr>
                  <th scope="row">
                    Round 1
                  </th>
                  <!--Round1에 빈칸으로! 이상 있을 시 check-->
                  <td
                    v-for="(playerNick, index) in playerList"
                    :key="index">
                  </td>
                </tr>

                <!-- 1 Round Log -->
                <!-- r1LogList:
                  [['1토리토리','1토리연두','1토리쌀쫑','1토리보리','1토리소금'], 
                  ['연두토리','연두연두','연두쌀쫑','연두보리','연두소금'], 
                  ['쌀쫑토리','쌀쫑연두','쌀쫑쌀쫑','쌀쫑보리','쌀쫑소금'],
                  ['보리토리','보리연두','보리쌀쫑','보리보리','보리소금'], 
                  ['소금토리','소금연두','소금쌀쫑','소금보리','소금소금']] -->
                <tr
                  v-for="(turnLog, index) in r1LogList"
                  :key="index">
                  <th scope="row">
                    {{ playerList[index] }}
                  </th>
                  <td
                    v-for="(money, idx) in turnLog"
                    :key="idx">
                    {{ money }}
                  </td>
                </tr>
                <!--2 Round 구분행 생성-->
                <tr>
                  <th scope="row">
                    Round 2
                  </th>
                  <div
                    v-for="(playerNick, index) in playerList"
                    :key="index">
                    <td>
                    </td>
                  </div>
                </tr>
                <!-- 2 Round Log -->
                <!-- 
                [['2연두토리','2토리연두','2토리쌀쫑','2토리보리','2토리소금'], 
                ['연두토리','연두연두','연두쌀쫑','연두보리','연두소금'],
                ['쌀쫑토리','쌀쫑연두','쌀쫑쌀쫑','쌀쫑보리','쌀쫑소금'], 
                ['보리토리','보리연두','보리쌀쫑','보리보리','보리소금'], 
                ['소금토리','소금연두','소금쌀쫑','소금보리','소금소금']], -->
                <tr
                  v-for="(turnLog, index) in r2LogList"
                  :key="index">
                  <th scope="row">
                    {{ playerList[index] }}
                  </th>
                  <td
                    v-for="(money, idx) in turnLog"
                    :key="idx">
                    {{ money }}
                  </td>
                </tr>

                <!--3 Round 구분행 생성-->
                <tr>
                  <th scope="row">
                    Round 3
                  </th>
                  <div
                    v-for="(playerNick, index) in playerList"
                    :key="index">
                    <td>
                    </td>
                  </div>
                </tr>
                <!-- 3 Round Log -->
                <!-- 
                [['3쌀쫑토리','3토리연두','3토리쌀쫑','3토리보리','3토리소금'], 
                ['연두토리','연두연두','연두쌀쫑','연두보리','연두소금'], 
                ['쌀쫑토리','쌀쫑연두','쌀쫑쌀쫑','쌀쫑보리','쌀쫑소금'], 
                ['보리토리','보리연두','보리쌀쫑','보리보리','보리소금'], 
                ['소금토리','소금연두','소금쌀쫑','소금보리','소금소금']], -->
                <tr
                  v-for="(turnLog, index) in r3LogList"
                  :key="index">
                  <th scope="row">
                    {{ playerList[index] }}
                  </th>
                  <td
                    v-for="(money, idx) in turnLog"
                    :key="idx">
                    {{ money }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    
    <!-- 게임 랭크 모달 버튼 [게임 순위] -->
    <button
      type="button"
      class="btn mj-btn rule-info"
      data-bs-toggle="modal"
      style="margin-right: 10px;"
      data-bs-target="#RankModal">
      🏆게임 순위🏆
    </button>


    <!-- 게임 랭크 확인 로그 모달 -->
    <div
      class="modal fade"
      id="RankModal"
      role="dialog"
      tabindex="-1"
      aria-labelledby="RankModal Label"
      aria-hidden="true">
      <div
        class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
        <div
          class="modal-content">
          <div class="modal-header">
            <h5
              class="modal-title"
              id="RankModal
              Label">
              🎲 게임 등수 확인 🎲
            </h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close">
            </button>
          </div>
          <!-- 게임 등수 확인하는 테이블 -->
          <div
            class="modal-body"
            style="max-width: 100%; width: auto; display: table;">
            <!-- 열: 인원수 * 라운드 -->
            
            <table class="table">
              <thead>
                <tr>
                  <th scope="col">
                    등수
                  </th>
                  <th scope="col">
                    닉네임
                  </th>
                  <th scope="col">
                    최종금액
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="(user,index) in gameRank"
                  :key="index">
                  <!-- 등수 -->
                  <th scope="row">
                    {{ user[0]+1 }}등
                  </th>
                  <!-- 유저 닉네임 -->
                  <td>
                    {{ user[1] }}
                  </td>
                  <!-- 최종금액 -->
                  <td>
                    {{ user[2] }}만원
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <button
      type="button"
      class="btn mj-btn-reverse"
      @click=" this.$router.push({ name: 'Home' })">
      메인화면으로
    </button>
  </div>
</template>

<script>
export default {
	data() {
		return {
			winner: '',
      playerList: ['토리','연두','쌀쫑','보리','소금'],
      r1LogList: [['1토리토리','1토리연두','1토리쌀쫑','1토리보리','1토리소금'], ['연두토리','연두연두','연두쌀쫑','연두보리','연두소금'], ['쌀쫑토리','쌀쫑연두','쌀쫑쌀쫑','쌀쫑보리','쌀쫑소금'], ['보리토리','보리연두','보리쌀쫑','보리보리','보리소금'], ['소금토리','소금연두','소금쌀쫑','소금보리','소금소금']],
      r2LogList: [['2연두토리','2토리연두','2토리쌀쫑','2토리보리','2토리소금'], ['연두토리','연두연두','연두쌀쫑','연두보리','연두소금'], ['쌀쫑토리','쌀쫑연두','쌀쫑쌀쫑','쌀쫑보리','쌀쫑소금'], ['보리토리','보리연두','보리쌀쫑','보리보리','보리소금'], ['소금토리','소금연두','소금쌀쫑','소금보리','소금소금']],
      r3LogList: [['3쌀쫑토리','3토리연두','3토리쌀쫑','3토리보리','3토리소금'], ['연두토리','연두연두','연두쌀쫑','연두보리','연두소금'], ['쌀쫑토리','쌀쫑연두','쌀쫑쌀쫑','쌀쫑보리','쌀쫑소금'], ['보리토리','보리연두','보리쌀쫑','보리보리','보리소금'], ['소금토리','소금연두','소금쌀쫑','소금보리','소금소금']],
      gameRank: [[0,'채은', 30000],[1, '가은', 400], [2, '머머리', -4700]],
		}
	},
  mounted() {
    // 전체 결과 받아오기"i+1등 닉네임 금액"
    this.emitter.on('logFinalRank', rank => this.showRank(rank))
    // 3차원 로그 리스트 담아줌"라운드/턴/닉네임"
    this.emitter.on('FinalGamePlayers', log => this.createPlayerList(log))
    // 플레이어 담아줌
    this.emitter.on('FinalGameLog', log => this.createTable(log))
  },
  methods: {
    createPlayerList(list){
      this.playerList = list;
      console.log("플레이어 리스트를 순서대로 반환 : " + list);
    },
    createTable(log) {
      this.r1LogList = log[0]
      this.r2LogList = log[1]
      this.r3LogList = log[2]
      console.log("최종 게임 로그 : " + log);   
    },
    showRank(rank) {
      var finalrank = JSON.parse(rank);
      console.log("최종 1등! : " + finalrank[0].nickName);
      this.winner = finalrank[0].nickName;
      for (var i = 0; i < finalrank.length; i++) {
        // [i+1등 , 닉네임, 최종 금액]
        this.gameRank.push([i, finalrank[i].nickName], finalrank[i].money)
        // console.log(i + 1 + "등의 닉네임 : " + finalrank[i].nickName);
        // console.log(i + 1 + "등의 최종 금액 : " + finalrank[i].money);
      }
    },
  }
}
</script>

<style scoped>
.modal-backdrop.show {
    opacity: 0.5;
    z-index: -1;
}

.modal.fade.show {
  height: 200%;
  overflow-x: hidden;
  overflow-y: hidden;
}

.modal-dialog {
  max-width: 1000px;
  margin: 0.75rem auto;
}

.mj-btn {
  width: 200px;
  margin-top: 10px;
  display: inline-block;
}

.mj-btn-reverse {
  width: 200px;
  margin-top: 10px;
}
</style>
