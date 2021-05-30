<template>
	<div class="root" :class="addDarkClass()" >
		<div class="container">
			<div class="item shorthand" @mouseenter="enter(i)" @mouseleave="leave(i)" v-for="(item,i) in dataList"
			     :key="item.createTime">
				<p>{{ item.content }}</p>
				<div class="time">
					<a href="javascript:void(0)" @click="handleClick(item.managerId,$event)" class="nickName">{{ item.nickname }}</a>
					<span class="publishTime">{{ item.publishTime }}</span>
				</div>
			</div>
		</div>
		<div  class="tip" v-if="dataList.length===0">
			<span>没有发布任何速记，可以前往后台首页发送哦。</span>
		</div>
	<UserInfo :startX="x" :startY="y" :userId="userId" ></UserInfo>
	</div>
</template>

<script setup lang="ts">
import UserInfo from '../components/UserInfo.vue'
import FullScreen from '../components/FullScreen.vue'
import {onMounted, reactive, ref} from "vue";
import {get} from "../http";
import {addDarkClass} from "../utils/common";
let x=ref(0);
let y=ref(0);
let userId=ref()
function handleClick(id:number,event:any) {
	x.value=event.clientX
	y.value=event.clientY
	userId.value=id;
}



onMounted(() => {
	getShortHand()
})
let dataList = reactive<any>([]);
let page = 1;
async function getShortHand() {
	const response = await get(`/shorthand/list?page=${page}&limit=10`);
	if (response.code == 200) {
		response.data.list.forEach((value: any)=>dataList.push(value))
	}
}
//下面都是样式
let scale = reactive<any>({})
const ele: any = document.getElementsByClassName("shorthand");

function enter(index: any) {
	ele[index].style.transform = "scale(1.03)"
}

function leave(index: any) {
	ele[index].style.transform = "scale(1)"
}
</script>

<style scoped lang="scss">
.root {
	background-color: #eef5ff;
	height: 100vh;
	width: 100vw;
	overflow: auto;

	.container {
		padding-top: 100px;

		.item {
			width: 800px;
			margin: 30px auto;
			box-sizing: border-box;
			padding: 30px;
			background-color: white;
			height: 100px;
			border-radius: 10px;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			transition: all 0.3s ease;

			p {
				font-size: 15px;
				text-indent: 10px;
			}

			.time {
				width: 100%;
				color: #525252;
				display: flex;
				justify-content: flex-end;

				.nickName {
					margin-right: 10px;
					font-size: 13px;
					text-decoration: none;
					color: #0b9aff;
					&:hover{
						text-decoration-line: underline;
					}
				}

				.publishTime {
					right: 0;
					bottom: 5px;
					text-align: right;
					font-size: 12px;
					font-style: italic;
				}

			}
		}
	}

	.tip{
		margin-top: 300px;
		text-align: center;
		font-size: 20px;
	}
}

.root.dark{
	background-color: #0d1117;
	.container {
		.item {
			background-color: #161b22;
			.time {
				color: #848484;
				.nickName {
					color: #737bbd;
					&:hover{
						text-decoration-line: underline;
					}
				}
			}
		}
	}
}
</style>