<template>
  <div>
    <button class="mj-btn">
      {{ btnText }}
    </button>
  </div>
</template>

<script>
export default {

  props: {
    btnText: {
      type: String,
      default: "default"
    },
  },

}
</script>

<style>

.mj-btn {
  position: relative;
  width: 250px;
  padding: 10px;
  border: 2px solid #333;
  border-radius: 4px;
  color: #333;
  font-weight: 700;
  font-size: 20px;
  text-align: center;
  cursor: pointer;
  box-sizing: border-box;
  display: block;
  transition: .4s;
  margin: 0 auto;
  margin-top: 40px;
  background-color: #333;
  color: #fff;

}

.mj-btn-reverse {
  position: relative;
  width: 250px;
  padding: 10px;
  border: 2px solid #fff;
  border-radius: 4px;
  color: #fff;
  font-weight: 700;
  font-size: 20px;
  text-align: center;
  cursor: pointer;
  box-sizing: border-box;
  display: block;
  transition: .4s;
  margin: 0 auto;
  margin-top: 40px;
  background-color: #fff;
  color: #333;

}

.mj-btn:hover {
  background-color: rgb(223, 223, 223);
  color: #333;
}

.mj-btn-reverse:hover {
  background-color: #333;
  color: #fff;
}

</style>