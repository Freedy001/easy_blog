<!--suppress ALL -->
<template>
	<div class="full-screen" @click="$emit('clickOutSide')" :style="{'backgroundColor':`rgba(0, 0, 0,${opacity})`,'zIndex':index}">
		<div @click.stop="">
			<slot></slot>
		</div>
	</div>
</template>

<script setup lang="ts">
import {defineEmit, defineProps} from "vue";

defineProps(['opacity','index'])
defineEmit(['clickOutSide'])
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