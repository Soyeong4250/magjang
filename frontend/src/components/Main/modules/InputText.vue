<template>
  <div class="mj-input-group input-group-lg">
    <input
      type="text"
      class="form-control"
      aria-label="Sizing example input"
      aria-describedby="inputGroup-sizing-lg"
      :placeholder="placeText"
      @keypress.enter="$emit('enter')"
      @keyup="$emit('child-input-change', inputText)"
      @input="inputText = $event.target.value" />
  </div>
</template>

<script>
export default {
  props: {
    placeText: {
      type: String,
      default: "default"
    },
  },
  data() {
    return {
      inputText: "",
    }
  },
  emits: ['child-input-change', 'enter'],
}
</script>

<style scoped>

.mj-input-group {
  position: relative;
  text-align: center;
  width: 400px;
  margin: 0 auto;
}

</style>