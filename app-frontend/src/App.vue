<template>
	<LoadingTab v-if="isLoading"></LoadingTab>
	<div class="logo-and-menu">
		<img @click="$router.push('/')" :src="loadResource($store.state.indexSetting.logo)" alt="">
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
</template>

<script setup lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import LoadingTab from './components/LoadingTab.vue'
import Menu from './components/Menu.vue'
import MenuLogo from './components/Menu.vue'
import router from "./router";
import {get, loadResource} from "./http";
import {useStore} from "vuex";
const store = useStore();
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
	getIndexSetting()
	heartBeat();
})

async function getIndexSetting() {
	const response =await get('/sys/getIndexSetting');
	if (response.code==200){
		const data:any = response.data;
		store.commit('setIndexSetting',data)
	}
}
//没5s 发送一次心跳让服务器知道此用户在线
async function heartBeat() {
	setInterval(()=>{
		const sys = get('/sys/heartbeat');
	},5000)
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
	img{
		margin-left: 30px;
		cursor: pointer;
		width: 65px;
		height: 65px;
		border-radius: 50%;
		transition: all 0.5s ease;
		&:hover{
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
		.line{
			width: 30px;
			height: 4px;
			background-color: rgb(114, 114, 114);
			border-radius: 50px;
			transition: all .3s ease;
		}
		&:hover{
			flex-direction: row;
			.line{
				height: 30px;
				width: 5px;
				background-color: rgb(9, 136, 146);
			}
		}
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
img{
	user-select: none;
}
</style>