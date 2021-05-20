<template>
	<div class="root">
		<div class="container">
			<div class="item shorthand" @mouseenter="enter(i)" @mouseleave="leave(i)" v-for="(item,i) in dataList">
				<p>{{item.content}}</p>
				<p>{{item.managerId}}</p>
				<div class="time">
					<span>{{item.createTime}}</span>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

import {onMounted, reactive, ref} from "vue";
import {get} from "../http";

onMounted(()=>{
	getShortHand()
})
let dataList=ref();
async function getShortHand() {
	const response = await get('/shorthand/list');
	if (response.code==200){
		dataList.value=response.data.list
	}
}

//下面都是样式
let scale=reactive<any>({})
const ele:any = document.getElementsByClassName("shorthand");
function enter(index:number) {
	ele[index].style.transform="scale(1.03)"
}
function leave(index:number) {
	ele[index].style.transform="scale(1)"
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
			p{
				font-size: 15px;
				text-indent: 10px;
			}
			.time {
				width: 100%;
				position: relative;

				span {
					position: absolute;
					right: 0;
					bottom: 5px;
					text-align: right;
					font-size: 12px;
					font-style: italic;
				}

			}
		}
	}
}
</style>