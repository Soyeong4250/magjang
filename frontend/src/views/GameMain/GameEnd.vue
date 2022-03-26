<template>
  <div 
    id="game-end" 
    class="container row"
    style="margin-top:100px">
    <div class="text-center">
      <h1 class="winner-text">
        {{ winner }} 님이 우승하셨습니다.
      </h1>
      <button
        type="button"
        class="btn mj-btn-reverse"
        @click=" this.$router.push({ name: 'Home' })">
        게임로그
      </button>
      <button
        type="button"
        class="btn mj-btn-reverse"
        @click=" this.$router.push({ name: 'Home' })">
        메인화면으로
      </button>
      <button
        type="button"
        class="btn mj-btn-reverse"
        @click=" this.$router.push({ name: 'GameView' })">
        다시하기
      </button>
    </div>
  </div>
</template>

<script>

export default {
	data() {
		return {
			winner : '한채은',
		}
	},

}
</script>

<style>

#game-end {
  width: 100%;
  background-color: #1f1f1f;

}

.winner-text {
	color : white;
}

#game-end .btn {
  left : 50vh;
}
</style>