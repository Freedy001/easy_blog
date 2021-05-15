<template>
	<LoadingTab v-if="isLoading"></LoadingTab>
	<router-view v-slot="{ Component }">
		<transition enter-active-class="fade-in-right"
		            leave-active-class="fade-out-left"
		            mode="out-in">
			<component :is="Component"/>
		</transition>
	</router-view>
</template>

<script setup lang="ts">
import {defineComponent, ref} from "vue";
import LoadingTab from './components/LoadingTab.vue'
import {useRouter} from "vue-router";
defineComponent({
	LoadingTab
})
const router = useRouter();
let isLoading=ref(false)
router.beforeEach((to, from)=>{
	isLoading.value=true;
})
router.afterEach(()=>{
	setTimeout(()=>{
		isLoading.value=false
	},500)
})
</script>

<style lang="scss">
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

*{
	margin: 0;
	padding: 0;
	list-style: none;
	box-sizing: border-box;
	&::-webkit-scrollbar {
		/*滚动条整体样式*/
		width : 5px;  /*高宽分别对应横竖滚动条的尺寸*/
		height: 1px;
	}
	&::-webkit-scrollbar-thumb {
		/*滚动条里面小方块*/
		border-radius: 10px;
		box-shadow   : inset 0 0 5px rgba(0, 0, 0, 0.2);
		background   : #7abbfd;
	}
}
body{
	overflow: hidden;
}
#app{
	height: 100vh;
	width: 100vw;
	display: flex;
	background: linear-gradient(30deg, #00ccff, #00b9ff, #3a9ff5, #1e8bfd);
	background-size:1400% 300%;
	animation:mymove 5s ease-in-out infinite alternate;

}
@keyframes mymove {
	0%{
		background-position: 0% 0%;
	}
	50%{
		background-position: 50% 100%;
	}
	100%{
		background-position: 100% 0%;
	}
}

h2.tit{
	border-left: 2px solid var(--color-main);
	padding-left: 16px;
	font-size: 18px;
	font-weight: 400;
	margin: 20px 0 30px;
	color: var(--color-main);
	display: flex;
	align-items: center;
	justify-content: space-between;
	.add{
		height: 30px;
		line-height: 32px;
		font-size: 13px;
		border-radius: 4px;
		padding: 0 12px;
		color: var(--color-main);
		cursor: pointer;
		background: #e8f4ff;
		cursor: pointer;
		&:hover{
			color: #fff;
			background: var(--color-main);
		}
		.el-icon-plus{
			font-size: 12px;
			font-weight: 800;
		}
	}
	:root{
		--color-main: #0084ff;
		--color-main-hover: #0279e9;
	}
}
</style>