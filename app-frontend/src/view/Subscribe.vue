<template>
	<div class="root">
		<video autoplay="autoplay"
		       preload
		       id="video" loop>
			<source :src="loadResource('/resource/ocien.mp4')" type="video/mp4"/>
			Your browser does not support the video tag.
		</video>
		<div class="card">
			<el-card class="box-card">
				<h1>Hi,感谢你发现了我</h1>
				<p>心里藏着小星星，生活才能亮晶晶心里藏着小星星！</p>
				<span class="tip">如果你喜欢我的博客的话,可以订阅啊😉</span>
				<div class="email-input">
					<input type="text" placeholder="你的邮箱地址" v-model="email" @keypress.enter="subscribe">
					<div @click="subscribe"><span>订阅</span></div>
				</div>
			</el-card>
		</div>
	</div>
</template>

<script setup lang="ts">
import {get, loadResource} from "../http";
import {ref} from "vue";
let email=ref()

async function subscribe() {
	// const response = get('');
}
</script>

<style scoped lang="scss">
#video {
	position: absolute;
	top: 0;
	left: 0;
	width: 100vw;
	height: 100vh;
	object-fit: cover;
	z-index: -99999;
}

.card {
	width: 100%;
	position: absolute;
	z-index: 500;
	top: 50%;
	transform: translateY(-50%);
	display: flex;
	align-items: center;
	justify-content: center;

	.box-card {
		background-color: rgba(255, 255, 255, 0.54);
		width: 450px;
		user-select: none;
		border-radius: 20px;
		h1 {
			text-align: center;
			margin-bottom: 20px;
		}
		p{
			text-align: center;
		}
		.tip{
			margin: 20px;
			text-align: center;
			display: block;
		}
		.email-input {
			display: flex;
			justify-content: space-around;
			input{
				outline: none;
				height: 35px;
				width: 250px;
				border-radius: 20px;
				border: 1px solid #878787;
				background-color: rgba(255, 255, 255, 0.66);
				text-indent: 20px;
				transition: all 0.3s ease;
				color: #898989;
				&::-webkit-input-placeholder {
					color: #a1a1a1;
					font-size: inherit;
					text-indent: 20px
				}
				&:focus {
					border: 1px solid #56abe8;
				}
			}
			div{
				border: 1px solid #878787;
				width: 100px;
				height: 34px;
				border-radius: 20px;
				background-color: rgba(255, 255, 255, 0.66);
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: inherit;
				transition: all 0.2s ease;
				&:hover{
					background-color: rgba(255, 255, 255, 0.9);
				}
			}
		}
	}
}
</style>