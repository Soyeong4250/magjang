<template>
  <div class="base-timer">
    <svg
      class="base-timer__svg"
      viewBox="0 0 100 100"
      xmlns="http://www.w3.org/2000/svg">
      <g class="base-timer__circle">
        <circle
          class="base-timer__path-elapsed"
          cx="50"
          cy="50"
          r="46.5" />
        <path
          :stroke-dasharray="circleDasharray"
          :class="remainingPathColor"
          class="base-timer__path-remaining"
          d="
            M 50, 50
            m -45, 0
            a 45,45 0 1,0 90,0
            a 45,45 0 1,0 -90,0
          " />
      </g>
    </svg>
    <span
      class="base-timer__label">
      {{ formattedTimeLeft }}
    </span>
  </div>
</template>

<script>
export default {
  props: {
    timeLeft: {
      type: Number,
      required: true
    },
    alertThreshold: {
      type: Number,
      default: 10
     },
    warningThreshold: {
      type: Number,
      default: 20
    },
  },
  data() {
    return {
      // 겉에 원형 색깔 줄어드는 초
      timeLimit: 180,
      timePassed: 0,
      timerInterval: null,
    }
  },

  computed: {
    formattimeLeft() {
      return this.timeLimit - this.timePassed
    },
		formattedTimeLeft() {
      const timeLeft = this.timeLeft
      const minutes = Math.floor(timeLeft / 60)
      let seconds = timeLeft % 60
			
      // this.emitter.emit('timeLeft')
      if (seconds < 10) {
        seconds = `0${seconds}`
      } 
      // The output in MM:SS format
      return `${minutes}:${seconds}`
    },
    // Update the dasharray value as time passes, starting with 283
    circleDasharray() {
      return `${(this.timeFraction * 283).toFixed(0)} 283`;
    },
    timeFraction() {
      const rawTimeFraction = this.timeLeft / this.timeLimit
      
      return rawTimeFraction - (1 / this.timeLimit) * (1 - rawTimeFraction)
    },
    colorCodes() {
      return {
        info: {
          color: "green"
        },
        warning: {
          color: "orange",
          threshold: this.warningThreshold
        },
        alert: {
          color: "red",
          threshold: this.alertThreshold
        }
      }
    },
    remainingPathColor() {
       const { alert, warning, info } = this.colorCodes;
       if (this.timeLeft <= alert.threshold) {
         return alert.color;
       } else if (this.timeLeft <= warning.threshold) {
         return warning.color;
       } else {
         return info.color;
       }
    }
  }
}
</script>

<style scoped lang="scss">
/* Sets the containers height and width */
.base-timer {
  position: relative;
  width: 150px;
  height: 150px;
  margin: 0 auto;
	
/* Removes SVG styling that would hide the time label */
  &__circle {
    fill: none;
    stroke: none;
  }
	
/* The SVG path that displays the timer's progress */
  &__path-elapsed {
    stroke-width: 7px;
    stroke:grey;
  }

  &__label {
    position: absolute;    
    
    /* Size should match the parent container */    
    width: 150px;
    height: 150px;
    
    /* Keep the label aligned to the top */    
    top: 0;
    
    /* Create a flexible box that centers content vertically and horizontally */    
    display: flex;
    align-items: center;
    justify-content: center;
    
    /* Sort of an arbitrary number; adjust to your liking */
    font-size: 40px;
  }

  &__path-remaining {
    /* Just as thick as the original ring */
    stroke-width: 7px;
		
    /* Rounds the line endings to create a seamless circle */
    stroke-linecap: round;
		
    /* Makes sure the animation starts at the top of the circle */
    transform: rotate(90deg);
    transform-origin: center;
		
    /* One second aligns with the speed of the countdown timer */
    transition: 1s linear all;
		
    /* Allows the ring to change color when the color value updates */
    stroke: rgb(65, 184, 131); // green
  }
	
  &__svg {
    transform: scaleX(-1);
  }

  &__path-remaining {
    stroke-width: 7px;
    stroke-linecap: round;
    transform: rotate(90deg);
    transform-origin: center;
    transition: 1s linear all;
    fill-rule: nonzero;
    stroke: currentColor;
		
    &.green {
      color: rgb(65, 184, 131);
    }
		
    &.orange {
      color: orange;
    }
		
    &.red {
      color: red;
    }
  }
}
</style>