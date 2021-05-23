<template>
	<teleport to="body">
		<FullScreen :opacity="opacity" id="full-loading">
			<transition name="el-fade-in-linear">
				<img :src="moon" class="icon" :class="{'moon-animate':animate}" v-show="showMoon" alt="">
			</transition>
			<transition name="el-fade-in-linear">
				<img :src="sun" class="icon" :class="{animate:animate}" v-show="showSum" alt="">
			</transition>
		</FullScreen>
	</teleport>
</template>

<script setup lang="ts">
import FullScreen from './FullScreen.vue'
import {defineComponent, defineEmit, defineProps, getCurrentInstance, onMounted, ref, watch} from "vue";
import sun from '../assets/icon/Sun.svg'
import moon from '../assets/icon/moon.svg'
import {changeMode, isDarkMode} from "../utils/common";
defineComponent({
	FullScreen
})
const {proxy}: any = getCurrentInstance();

let opacity = ref(0.8)
let animate = ref(false)
let showMoon = ref(isDarkMode())
let showSum = ref(!isDarkMode())
onMounted(()=>{
	showMoon.value=isDarkMode()
	showSum.value=!isDarkMode()
	animate.value = true
	setTimeout(() => {
		showMoon.value = true
		showSum.value = true
	}, 400)
	setTimeout(() => {
		showMoon.value = !isDarkMode()
		showSum.value = isDarkMode()
	}, 600)
	setTimeout(()=>{
		changeMode()
	},1500)
	setTimeout(() => {
		showMoon.value = false
		showSum.value = false
		opacity.value=0
	}, 2000)
})


</script>

<style scoped lang="scss">
.icon {
	width: 800px;
	height: 800px;
	border-radius: 100%;
	position: absolute;
	left: calc(50vw - 400px);
	top: calc(50vh - 400px);

}

.moon-animate {
	animation: moon-animate 1s cubic-bezier(.04,.51,.1,1) both;
}

@keyframes moon-animate {
	0% {
		transform: rotateZ(0);
	}
	100% {
		transform: rotateZ(360deg);
	}
}

.animate {
	animation: inMode 1s ease both;
}

@keyframes inMode {
	0% {
		transform: rotateZ(0);
	}
	100% {
		transform: rotateZ(360deg);
	}
}

</style>