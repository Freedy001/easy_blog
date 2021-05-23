<template>
	<popBox :startX="startX" :startY="startY" size="Middle">
		<div class="expand" :class="addDarkClass()">
			<div class="info">
				<div class="photo">
					<img :src="loadResource(userInfo.headImg)" alt="">
				</div>
				<p class="name">{{ userInfo.nickname }}</p>
			</div>
			<div class="introduce">
				<div class="item day">
					<div>
						<i class="el-icon-sunny" style="margin-right:3px"></i>
						<span> {{ userInfo.createDuration }} </span>
					</div>
				</div>
				<div class="item email">
					<div class="email-box">
						<img style="margin-right: 6px" :src="isDarkMode()?emailDark:email" alt="">
						<span> {{ userInfo.email }} </span>
					</div>
				</div>
				<div class="item text">
					<div>
						<span>个人介绍</span>
						<p> {{ userInfo.introduce }} </p>
					</div>
				</div>
			</div>
		</div>
	</popBox>
</template>

<script setup lang="ts">
import {get, loadResource} from "../http";
import FullScreen from './FullScreen.vue'
import popBox from './popBox.vue'
import email from '../assets/icon/email.svg'
import emailDark from '../assets/icon/email-dark.svg'
import {defineComponent, defineProps, getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {addDarkClass, copyProperties, isDarkMode} from "../utils/common";
defineProps(['startX', 'startY', 'userId', 'nickname'])
defineComponent({
	FullScreen
})
const {proxy}: any = getCurrentInstance();
let userInfo = reactive({})

watch(() => proxy.userId, async (val) => {
	const response = await get(`/manager/infoById?id=${val}`);
	if (response.code == 200) copyProperties(response.data, userInfo)
})

watch(() => proxy.nickname, async (val) => {
	const response = await get(`/manager/infoByNickname?nickname=${val}`);
	if (response.code == 200) copyProperties(response.data, userInfo)
})

</script>

<style scoped lang="scss">
.expand {
	margin: auto 0;
	width: 100%;
	height: 80%;
	padding: 25px 10px 0 10px;
	box-sizing: border-box;


	.info {
		.photo {
			width: 70px;
			height: 70px;
			margin: auto;
			border-radius: 50%;
			background: #fff;
			overflow: hidden;
			transition: all .3s;

			img {
				width: 100%;
				height: 100%;
			}

		}

		.name {
			margin: 10px 0 10px 0;
			color: #000000;
			text-align: center;
			font-size: 18px;
		}
	}

	.introduce {
		margin: 10px;

		.item {
			margin: 10px;
			display: flex;
			justify-content: center;
			align-items: center;

			div {
				display: flex;
				align-items: center;
			}
		}

		.text {
			margin-top: 15px;

			div {
				display: block;
				overflow: auto;
				height: 300px;

				&::-webkit-scrollbar {
					width: 0;
				}

				span {
					margin-top: 5px;
					display: block;
					text-align: center;
					margin-bottom: 10px;
				}

				p {
					display: block;
					margin-top: 15px;
				}
			}
		}
	}
}

.expand.dark{
	background-color: #0d1117;
	border: none;
	color: #c9c9c9;

	.name {
		color: #ffffff;
	}
}

</style>