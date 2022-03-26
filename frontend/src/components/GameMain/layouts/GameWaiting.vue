<template>
  <div
    class="button-flex">
    <button
      type="button"
      class="btn mj-btn"
      @click="copyCode">
      입장 코드 <hr />
      {{ gameCode }} 
    </button>
    <!-- player가 host이고 4명 이상일 때 -->
    <button
      v-if="(host == nickName)&&(userCount >= 4)"
      type="button"
      class="btn game-start-btn mj-btn"
      @click="gameStart()">
      게임시작
    </button>
    <button
      v-else-if="host == nickName"
      type="button"
      class="btn game-start-btn mj-btn disabled">
      게임 인원이 <br />4명 이하입니다.
    </button>
    <!-- player가 host가 아닐때 -->
    <button
      v-else
      type="button"
      class="btn game-start-btn mj-btn disabled">
      호스트가 게임을 <br />시작합니다.
    </button>
    <button
      type="button"
      class="btn mj-btn"
      @click="$emit('go-to-main'), clickMain()">
      메인화면으로
    </button>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  emits: ['go-to-main'],
  data() {
    return {
      gameCode: this.$route.params.code,
      host: null,
      players: [],
      nickName: this.$store.state.nickName,
      userCount: 0,
    }
  },
  methods: {
    getPlayerList(playerList){
      // this.host = playerList[0]
      this.host = playerList[0]
      // this.host = "김주호"
      this.players = playerList
      this.userCount = Object.keys(playerList).length
      // this.userCount = 4
    },
    copyCode() {
      this.$copyText(this.gameCode)
      alert(this.gameCode + '복사되었습니다!')
    },
    gameStart(){
      if(this.host == this.nickName){ // this.host == this.nickname으로 바꾸기
        this.emitter.emit('IamHost','IamHost')
      }
      this.changeGamePossible(true)
    },
    clickMain() {
      // check
      this.emitter.emit('chat_disconnect')
    },
    ...mapActions([
      "changeGamePossible"
    ])
  },
  mounted() {
    this.emitter.on('hostNPlayers', playerlist => this.getPlayerList(playerlist))

  },
}


</script>

<style scoped>
.button-flex {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;
}

.button-flex .mj-btn {
  width: 200px;
  margin-top: 0;
  padding-top: 15px;
}

.button-flex .game-start-btn {
  width: 200px;
  margin-top: 0;
  padding-top: 15px;
  /*color: #198754;*/
  border: 2px solid #198754;
  background-color: #198754;
  color: #fff;
}

.game-start-btn:hover {
  background-color: rgb(223, 223, 223);
  color: #198754;
}

.sp {
  background-blend-mode: overlay;
  background-color: rgba(0, 0, 0);
}
</style>