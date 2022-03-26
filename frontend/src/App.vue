<template>
  <div id="app">
    <RouterView />
  </div>
</template>

<style>
#app {
  height: 100vh;
  overflow: hidden;
  background-color: #1f1f1f;
  font-family: 'Nanum Gothic', sans-serif;
}
.modal-backdrop.show {
    z-index: -1;
}
</style>