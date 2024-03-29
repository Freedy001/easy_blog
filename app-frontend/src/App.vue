<template>
	<LoadingTab v-if="isLoading"></LoadingTab>
	<div class="logo-and-menu">
		<img @click="$router.push('/')" :src="loadResource(store.state.indexSetting.logo)" alt=" ">
		<div class="icon" @click="isShowMenu=!isShowMenu">
			<div class="line"></div>
			<div class="line"></div>
			<div class="line"></div>
		</div>
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
	<FullScreenChanging v-if="store.state.modeChanging"></FullScreenChanging>
	<div id="footer" v-html="store.state.indexSetting.footInfo">
	</div>
</template>

<script setup lang="ts">
import {defineComponent, onMounted, reactive, ref, watch, watchEffect} from "vue";
import LoadingTab from './components/LoadingTab.vue'
import Menu from './components/Menu.vue'
import FullScreenChanging from './components/FullScreenChanging.vue'
import router from "./router";
import {get, loadResource} from "./http";
import {useStore} from "vuex";

const store = useStore();
defineComponent({
	LoadingTab,
	Menu,
	FullScreenChanging
})
let isLoading = ref(true)
let isShowMenu = ref(false)
router.beforeEach((to, from) => {
	isLoading.value = true;
})
router.afterEach(() => {
	setTimeout(() => {
		isLoading.value = false
	}, 500)
})
onMounted(() => {
	getIndexSetting().then(() => {
		init();
	})
	heartBeat();
	modeChange(store.state.darkMode)
})

function init() {
	changeFavicon(loadResource(store.state.indexSetting.logo)); // 动态修改网站图标
	if (store.state.indexSetting.blogTitle) {
		document.title = store.state.indexSetting.blogTitle; // 动态修改网站标题
	} else {
		document.title = 'EASY BLOG'; // 动态修改网站标题
	}
}

function changeFavicon(link: string) {
	let $favicon: any = document.querySelector('link[rel="icon"]');
	// If a <link rel="icon"> element already exists,
	// change its href to the given link.
	if ($favicon !== null) {
		$favicon.href = link;
		// Otherwise, create a new element and append it to <head>.
	} else {
		$favicon = document.createElement("link");
		$favicon.rel = "icon";
		$favicon.href = link;
		document.head.appendChild($favicon);
	}
}

watch(() => store.state.darkMode, modeChange)

function modeChange(val: boolean) {
	if (val) {
		document.body.style.backgroundColor = '#0d1117'
		document.body.style.color = '#b8b8b8'
	} else {
		document.body.style.backgroundColor = '#ffffff'
		document.body.style.color = '#000000'
	}
}


//获取首页设置
async function getIndexSetting() {
	const response = await get('/sys/getIndexSetting');
	if (response.code == 200) {
		const data: any = response.data;
		store.commit('setIndexSetting', data)
	}
}

//每5s 发送一次心跳让服务器知道此用户在线
function heartBeat() {
	setInterval(async () => {
		const sys = await get('/sys/heartbeat');
		const split = ("" + sys.code).split(',');
		split.forEach((item: any) => {
			if (item == 205) {
				//重新加载设置
				getIndexSetting().then();
			}
			if (item == 206) {
				store.commit('notifyReloadArticle')
			}
		})
	}, 5000)
}
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

	img {
		margin-left: 30px;
		cursor: pointer;
		width: 65px;
		height: 65px;
		border-radius: 50%;
		transition: all 0.5s ease;

		&:hover {
			background-color: #253236;
		}
	}

	.icon {
		width: 30px;
		height: 33px;
		border-radius: 5px;
		margin-right: 50px;
		cursor: pointer;
		background-color: rgba(255, 255, 255, 0);
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		transition: all .3s ease;

		.line {
			height: 4px;
			width: 30px;
			background-color: rgb(114, 114, 114);
			border-radius: 50px;
			transition: all .3s ease;
		}

		&:hover {
			flex-direction: row;

			.line {
				width: 5px;
				height: 30px;
				background-color: rgb(9, 136, 146);
			}
		}
	}
}

#footer {
	width: 100%;
	display: flex;
	justify-content: center;
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

.slide-in-top {
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

.slide-out-top {
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

	&::selection {
		background: rgb(132, 239, 112);
		color: #000000;
	}

	&::-webkit-scrollbar {
		/*滚动条整体样式*/
		width: 5px; /*高宽分别对应横竖滚动条的尺寸*/
		height: 0;
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
	transition: all 1s ease;
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

img {
	user-select: none;
}
</style>