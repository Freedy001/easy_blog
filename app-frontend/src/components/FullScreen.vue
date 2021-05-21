<!--suppress ALL -->
<template>
	<div class="full-screen" @click="$emit('clickOutSide')" :style="{'background-color':`rgba(0, 0, 0,${dynamicOpacity})`,'z-index':index}">
		<div @click.stop="">
			<slot></slot>
		</div>
	</div>
</template>

<script setup lang="ts">
import {defineEmit, defineProps, getCurrentInstance, onMounted, ref, watch} from "vue";
defineProps(['opacity','index'])
defineEmit(['clickOutSide'])
const {proxy}:any = getCurrentInstance();
//动态透明度
let dynamicOpacity=ref(0)
watch(()=>proxy.opacity,()=>{
	animate()
})
onMounted(()=>{
	animate()
})

function animate() {
	const number = (proxy.opacity-dynamicOpacity.value)/100;
	let interval = setInterval(()=>{
		dynamicOpacity.value+=number
	},5);
	setTimeout(()=>{
		clearInterval(interval)
	},500)
}



</script>

<style scoped lang="scss">
.full-screen {
	width: 100%;
	height: 100%;
	position: absolute;
	top: 0;
	left: 0;
	z-index: 1000;
	display: flex;
	align-items: center;
	justify-content: center;
}

</style>