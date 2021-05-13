<template>
	<LoadingTab v-if="isLoading"></LoadingTab>
	<div class="logo-and-menu">
		<img @click="$router.push('/')" :src="loadResource('/image/LOGO谷歌.png')" alt="">
		<svg class="icon" aria-hidden="true" @click="isShowMenu=!isShowMenu">
			<use xlink:href="#icon-menu1"></use>
		</svg>
	</div>
	<router-view v-slot="{ Component }">
		<transition enter-active-class="fade-in-right"
		            leave-active-class="fade-out-left"
								mode="out-in">
			<component :is="Component"/>
		</transition>
	</router-view>
	<transition enter-active-class="slide-in-top" leave-active-class="slide-out-top">
		<Menu v-if="isShowMenu" @clickCb="isShowMenu=false"></Menu>
	</transition>
</template>

<script setup lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import LoadingTab from './components/LoadingTab.vue'
import Menu from './components/Menu.vue'
import router from "./router";
import {get, loadResource} from "./http";
defineComponent({
	LoadingTab,
	Menu
})
let isLoading=ref(true)
let isShowMenu=ref(false)
router.beforeEach((to, from)=>{
	isLoading.value=true;
})
router.afterEach(()=>{
    setTimeout(()=>{
	    isLoading.value=false
    },500)
})
onMounted(()=>{

})
</script>

<style lang="scss">
.logo-and-menu {
	position: fixed;
	top: 3%;
	width: 100%;
	display: flex;
	justify-content: space-between;
	align-items: center;
	z-index: 1000;
	img{
		margin-left: 30px;
		cursor: pointer;
		width: 30px;
		height: 30px;
	}
	.icon {
		font-size: 20px;
		color: #000000;
		border-radius: 5px;
		margin-right: 50px;
		cursor: pointer;
	}
}
.fade-in-right {
	animation: fade-in-right 0.3s ease-in both;
}
@keyframes fade-in-right {
	0% {
		transform: translateX(50px);
		opacity: 0;
	}
	100% {
		transform: translateX(0);
		opacity: 1;
	}
}
.fade-out-left {
	animation: fade-out-left 0.3s ease-out both;
}
@keyframes fade-out-left {
	0% {
		transform: translateX(0);
		opacity: 1;
	}
	100% {
		transform: translateX(-50px);
		opacity: 0;
	}
}
.slide-in-top{
	animation: slide-in-top 0.3s cubic-bezier(0.250, 0.460, 0.450, 0.940) both;
}
@keyframes slide-in-top {
	0% {
		transform: translateY(-1000px);
		opacity: 0;
	}
	100% {
		transform: translateY(0);
		opacity: 1;
	}
}
.slide-out-top{
	animation: slide-out-top 0.3s cubic-bezier(0.550, 0.085, 0.680, 0.530) both;
}
@keyframes slide-out-top {
	0% {
		transform: translateY(0);
		opacity: 1;
	}
	100% {
		transform: translateY(-1000px);
		opacity: 0;
	}
}


* {
	margin: 0;
	padding: 0;

	&::-webkit-scrollbar {
		/*滚动条整体样式*/
		width: 5px; /*高宽分别对应横竖滚动条的尺寸*/
		height: 1px;
	}

	&::-webkit-scrollbar-thumb {
		/*滚动条里面小方块*/
		border-radius: 10px;
		box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
		background: #7abbfd;
	}
}

body {
	margin: 0;
	padding: 0;
}

#app {
	height: 100%;
	width: 100%;
}

.icon {
	width: 1em;
	height: 1em;
	vertical-align: -0.15em;
	fill: currentColor;
	overflow: hidden;
}
:root {
	--animate-duration: 2s;
}
</style>