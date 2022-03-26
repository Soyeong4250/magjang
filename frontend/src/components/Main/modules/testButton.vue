<template>
  <b-button @click="testFunction">
    TEST
  </b-button>
</template>

<script>
export default {
  methods: {
    testFunction() {
      const turnPrice = 1300;
      const dealLimitPeople = 2;
      const dealCondition = "창고,언변,로비".split(',');

      console.log({turnPrice, dealLimitPeople, dealCondition})
      
      const dealStateCount = {
        "선박": 1,
        "언변": 1,
        "창고": 1,
        "인맥": 1,
        "정보": 1,
        "로비": 1,
      };
      
      dealCondition.forEach(element => {
        dealStateCount[element] = 0
      });

      console.log(dealStateCount)
    
    }
  }
  
}
</script>

<style>

</style>