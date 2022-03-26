<template>
  <div class="price-box">
    <vue-number-input
      :min="100"
      :model-value="dealPrice"
      :inputtable="false"
      inline 
      controls
      center
      readonly /> 
    <span>만원</span>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  // props: {
  //   price: {
  //     type: Number,
  //     default: 0
  //   },
	// },
  computed: {
    ...mapGetters([
      "dealPrice"
    ])
  }
}
</script>

<style scoped>

.price-box {
    text-align: center;
    margin-top: 10px;
}

.price-box span {
    position: absolute;
    font-size: 1.5rem; 
    color : white;
    margin-left: 10px;
    font-weight: 700;
    margin-top: 3px;
}
</style>