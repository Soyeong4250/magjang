<template>
  <div>
    <InputText
      place-text="닉네임을 입력해 주세요" 
      @child-input-change="nickNameChanged"
      @enter="nickNameCreated" />
    <p class="text-white">
      {{ warning }}
    </p>
    <Buttons
      btn-text="생성"
      @click="nickNameCreated" />
  </div>
</template>

<script>
// import InputNickname from '@/components/Main/modules/InputNickname.vue'
import InputText from '@/components/Main/modules/InputText.vue'
import Buttons from '@/components/Main/modules/Buttons.vue'
import { mapActions } from 'vuex'
import axios from 'axios'
export default {
  components: {
    // InputNickname, 
    InputText,
    Buttons,
  },
  data(){
    return{
      nickName: "",
      warning: "",
    }
  },
  methods: {
    ...mapActions([
      'setNickName'
    ]),
    nickNameChanged(inputData) {
      this.nickName = inputData;
    },
    nickNameCreated() {
      axios.post('/account/signup', {
        "nickName": this.nickName,
        "userId": this.$store.getters.userId
      })
        .then(response => {
          console.log(response.data);  // 받는 거에 따라 닉네임 입력할지 안할지
          if (response.data == true) {
            this.setNickName(response.data)
          } else {
            this.warning = response.data
          }
        })
        .catch(error =>{
          console.log(error)
        });
    }
  },
}
</script>


<style scoped>
p{
  text-align: center;
  font-size: large;
  font-weight: bold;
}
</style>