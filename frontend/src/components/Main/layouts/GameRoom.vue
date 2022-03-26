<template>
  <div v-if="roomEnterFlag">
    <InputText
      place-text="초대 코드를 입력해 주세요"
      @child-input-change="enterCodeChanged" />
    <Buttons
      btn-text="게임 시작"
      @click="gameview" />
    <Buttons
      btn-text="뒤로가기"
      @click="roomEnterFlag = false, enterCode = Math.floor(Math.random() * 100)" />
  </div>
  <div v-else>
    <Buttons
      btn-text="방 개설" 
      @click="gameview" />
    <Buttons
      btn-text="방 참가"
      @click="roomEnterFlag = true" />
  </div>
</template>

<script>
import InputText from '@/components/Main/modules/InputText.vue'
import Buttons from '@/components/Main/modules/Buttons.vue'

export default {
  components: {
    InputText,
    Buttons,
  },
  methods: {
    gameview: function() {
      this.$router.push({ name: 'GameView', params: {code: this.enterCode} })
    },
    enterCodeChanged(inputData) {
      this.enterCode = inputData
    },
  },
  data(){
    return {
      roomEnterFlag: false,
      enterCode: Math.floor(Math.random() * 100),
    }
  },
}
</script>

<style>

</style>