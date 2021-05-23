<!--suppress CssInvalidPseudoSelector -->
<template>
	<div :class="darkModeClass('menu')">
		<div class="hide-card"></div>
		<searchInput @searchCb="$emit('clickCb')" class="search-input"></searchInput>
		<div class="menu-item">
			<div class="item" @click="$router.push('/');$emit('clickCb')" >index</div>
			<div class="shorthand item" @click="$router.push('/shorthand');$emit('clickCb')">shorthand</div>
			<div class="item" @click="$store.commit('changingMode');$emit('clickCb')">{{ !isDarkMode()?'dark mode':'light mode'}}</div>
			<div class="subscribe item" @click="$router.push('/subscribe');$emit('clickCb')">subscribe</div>
			<div class="about item" @click="$router.push('/about');$emit('clickCb')">about</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import {defineComponent, defineEmit, getCurrentInstance} from "vue";
import searchInput from "./SearchInput.vue"
import {useRouter} from "vue-router";
import {darkModeClass, isDarkMode} from "../utils/common";
defineEmit(['clickCb'])
const router = useRouter();
const {proxy}:any = getCurrentInstance();
defineComponent({
	searchInput
})


</script>

<style scoped lang="scss">
.menu {
	position: fixed;
	z-index: 800;
	background-color: rgba(255, 255, 255, 0.96);
	width: 100vw;
	height: 100vh;
	top: 0;

	.search-input{
		top: 30%;
	}

	.menu-item {
		top: 45%;
		position: relative;
		display: flex;
		justify-content: center;
		align-items: center;

		.item {
			margin: 30px;
			font-size: 25px;
			font-weight: lighter;
			cursor: pointer;
			color: #5c5a5a;
			font-family: "Helvetica Neue", serif;
		}
	}
}

.menu-dark {
	position: fixed;
	z-index: 800;
	background-color: rgba(13, 17, 23, 0.95);
	width: 100vw;
	height: 100vh;
	top: 0;

	.search-input{
		top: 30%;
	}

	.menu-item {
		top: 45%;
		position: relative;
		display: flex;
		justify-content: center;
		align-items: center;

		.item {
			margin: 30px;
			font-size: 25px;
			font-weight: lighter;
			cursor: pointer;
			color: #cdcccc;
			font-family: "Helvetica Neue", serif;
		}
	}
}
</style>