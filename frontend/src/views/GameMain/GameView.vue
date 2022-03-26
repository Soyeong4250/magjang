<template>
  <div id="game-main">
    <div class="container-fluid">
      <GameHeader />
      <GamePage />
    </div>
  </div>
</template>

<script>
import GameHeader from '@/components/GameMain/layouts/GameHeader.vue';
import GamePage from '@/components/GameMain/layouts/GamePage.vue';

export default {
  components: {
    GameHeader,
    GamePage,
  },

  data(){
    return{
      
    }
  },
  methods: {
    
  }
}
</script>

<style>
.nav-top {
  height: 40px;
}

.game-layout {
  position: relative;
}

.game-layout .game-table-el {
  display: flex;
  text-align: center;
  position: absolute;
  width: 80%;
  height: 30vh;
  margin: 0 auto;
  left: 50%;
  top: 52%;
  transform: translate(-50%, -50%);
  background-color: rgb(180, 180, 180);
  font-size: 30px;
  border-radius: 20px;
  align-items: center;
}
.game-layout .game-table-el::-webkit-scrollbar {
  display: none;
}

.game-layout .user-video-head {
  position: absolute;
  top: 10px;
  width: 100%;
}

.game-layout .user-video .col {
  height: 25vh;
  border-radius: 20px;
  /* border: solid black 2px; */
}

.game-layout .user-video-foot {
  position: absolute;
  top: 64vh;
  width: 100%;
}

.game-log {
  width: 100%;
  height: 45vh;
  /* border: solid rgb(67,66,47) 2px; */
  color: white;
  font-size: 30px;
  border-radius: 20px;
  margin-top: 10px;
}

.game-chatting {
  width: 100%;
  height: 45vh;
  color: white;
  font-size: 30px;
  border-radius: 20px;
  margin-top: 20px;
}
</style>