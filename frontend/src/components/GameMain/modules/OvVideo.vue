<template>
  <video
    autoplay
    class="video-size">
  </video>
</template>

<script>
export default {
	name: 'OvVideo',

	props: {
		// streamManager: Object,
		streamManager: {
      type: Object,
      default: null,
    }
	},

	mounted () {
		this.streamManager.addVideoElement(this.$el);
	},
};
</script>

<style>
	.video-size {
		position: absolute;
    width: 100%;
    height: 100%;
		z-index: -1;
	}
</style>