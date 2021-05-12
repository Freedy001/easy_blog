<template>
	<div class="container">
		<div class="loader" v-if="hasMore">
			<div class="dot" v-for="i in 5" :key="i"></div>
		</div>
		<div v-if="!hasMore" style="margin: auto;color: #909090">没有更多了😭😭~~</div>
	</div>
</template>

<script setup lang="ts">
import {defineProps} from "vue";

defineProps(['hasMore'])
</script>

<style scoped lang="scss">
.container {
	width: 100%;
	height: 180px;
	display: flex;
	align-items: center;
}

.loader {
	margin: auto;
	height: 34px;
	width: 100px;
	display: flex;
	align-items: center;
	justify-content: space-between;
	z-index: 99;
}

.dot {
	width: 15px;
	height: 15px;
	background: #3ac;
	border-radius: 100%;
	display: inline-block;
	animation: slide 1s infinite;
}

.dot:nth-child(1) {
	animation-delay: 0.1s;
	background: #32aacc;
}

.dot:nth-child(2) {
	animation-delay: 0.2s;
	background: #64aacc;
}

.dot:nth-child(3) {
	animation-delay: 0.3s;
	background: #96aacc;
}

.dot:nth-child(4) {
	animation-delay: 0.4s;
	background: #c8aacc;
}

.dot:nth-child(5) {
	animation-delay: 0.5s;
	background: #faaacc;
}

@-moz-keyframes slide {
	0% {
		transform: scale(1);
	}
	50% {
		opacity: 0.3;
		transform: scale(2);
	}
	100% {
		transform: scale(1);
	}
}

@-webkit-keyframes slide {
	0% {
		transform: scale(1);
	}
	50% {
		opacity: 0.3;
		transform: scale(2);
	}
	100% {
		transform: scale(1);
	}
}

@-o-keyframes slide {
	0% {
		transform: scale(1);
	}
	50% {
		opacity: 0.3;
		transform: scale(2);
	}
	100% {
		transform: scale(1);
	}
}

@keyframes slide {
	0% {
		transform: scale(1);
	}
	50% {
		opacity: 0.3;
		transform: scale(2);
	}
	100% {
		transform: scale(1);
	}
}
</style>