<template>
  <div class="price-box">
    <vue-number-input
      v-bind="selectedUser"
      v-model="price"
      :min="100"
      :max="dealPrice == 100 ? price : 10000"
      :step="100" 
      :inputtable="false"
      inline 
      center
      controls
      @update:model-value="onUpdate" /> 
    <span>만원</span>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  data() { 
    return {
      price: 100,
    };
  },
  props: {
    selectedUser: Boolean,
    index: {
      type: Number,
      default: -1
    }
  },
  methods: {
    onUpdate(newValue, oldValue) {
      if (newValue - oldValue > 0 || isNaN(oldValue)) {
        this.updatePrice({"value": -100, "index": this.index})
      } else {
        this.updatePrice({"value": 100, "index": this.index})
      }
    },
    isPlayer() {
      if (this.player)
        return this.totalPrice
      return this.price
    },
    ...mapActions([
      "updatePrice",
    ])
  },
  computed: {
    ...mapGetters([
      "dealPrice",
      "userPrice",
    ])
  },
  watch: {
    
  },
  unmounted() {
    this.updatePrice({"value": this.price, "index": this.index})
  }
}
</script>

<style scoped>

.price-box {
    text-align: center;
    margin-top: 10px;
}
/*
.price-box .price {
    font-size: 1.5rem;
    color : white;
    background-color: #1f1f1f;
    text-align:right;
}
*/
.price-box span {
    position: absolute;
    font-size: 1.5rem; 
    color : white;
    margin-left: 10px;
    font-weight: 700;
    margin-top: 3px;
}
/*
input창에서 증감버튼 없애기 
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.form-control {
  display: inline-block;
  width: 56%;
}
*/
</style>