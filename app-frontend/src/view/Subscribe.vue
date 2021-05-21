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
				<transition-group enter-active-class="slide-in-right" leave-active-class="slide-out-left">
					<div class="first" v-if="firstPage===1">
						<h1>Hi,感谢你发现了我</h1>
						<p>心里藏着小星星，生活才能亮晶晶！</p>
						<span class="tip">如果你喜欢我的博客的话,可以订阅啊😉</span>
						<span v-if="onError" style="color: red;font-size: 12px;position: absolute; left: 20px ;bottom: -25px">你输入的邮箱格式好像不正确😢</span>
						<div class="email-input">
							<input type="text" placeholder="你的邮箱地址" v-model="email" @keypress.enter="subscribe">
							<el-button round :loading="emailLoading" :class="{onError:onError}" @click="subscribe">
								<span>订阅</span>
							</el-button>
						</div>
					</div>
					<div class="second" v-if="firstPage===2">
						<h1>邮箱验证</h1>
						<p>为了保证邮箱的正确性，请输入我给你发送的验证码</p>
						<div class="email-input">
							<input type="text" placeholder="验证码" v-model="verifyCode" @keypress.enter="verify">
							<el-button round :loading="verifyLoading" @click="verify">订阅</el-button>
						</div>
						<span class="tip">没收到？<el-button @click="subscribe" :loading="reSendLoading" round>
						<div class="timeout" :style="runTime">{{ timeout }}s后</div>再发一条
					</el-button></span>
					</div>
					<div class="third" v-if="firstPage===3">
						<h1>心里藏着小星星，生活才能亮晶晶！</h1>
						<h2>您已经订阅了</h2>
					</div>
				</transition-group>
			</el-card>
		</div>
	</div>
</template>

<script setup lang="ts">
import {get, loadResource} from "../http";
import {getCurrentInstance, onMounted, reactive, ref} from "vue";
const proxy:any = getCurrentInstance()?.proxy;
let reSendLoading = ref(true)
let timeout = ref(60)
let runTime = reactive<any>({})
const interval = setInterval(() => {
	if (--timeout.value == 0) {
		clearInterval(interval)
		reSendLoading.value = false
		runTime['display'] = 'none'
	}
}, 1000);
let firstPage = ref(1)
//上面都是样式

let emailLoading = ref(false)
let email = ref()
let onError=ref(false)
let uuid: any;
//订阅
async function subscribe() {
	if (email.value==null||!email.value.match('^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$')){
		onError.value=true;
		setTimeout(()=>{
			onError.value=false;
		},3000)
		return;
	}
	emailLoading.value = true
	const response = await get(`/sys/subscribe?email=${email.value}`);
	if (response.code == 200) {
		uuid=response.data
		setTimeout(()=>{
			firstPage.value = 2
		},500)
	}else if (response.code == 3007) {
		proxy.$notify({
			title: '提示',
			message: response.msg
		})
		firstPage.value=3
		localStorage.setItem("subscribe",uuid)
	}
}

let verifyLoading = ref(false)
let verifyCode = ref()
//验证邮箱
async function verify() {
	verifyLoading.value=true
	const response = await get(`/sys/verify?code=${verifyCode.value}&UUID=${uuid}`);
	if (response.code==200){
		verifyLoading.value=false
		firstPage.value=3
		localStorage.setItem("subscribe",uuid)
	}
}
onMounted(()=>{
	if (localStorage.getItem('subscribe')){
		firstPage.value=3
	}
})

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
		overflow: hidden;
		background-color: rgba(255, 255, 255, 0.54);
		width: 450px;
		height: 250px;
		user-select: none;
		border-radius: 20px;
		position: relative;

		h1 {
			text-align: center;
			margin-bottom: 20px;
		}

		p {
			text-align: center;
		}

		.tip {
			margin: 20px;
			text-align: center;
			display: block;
		}

		.email-input {
			display: flex;
			justify-content: space-around;

			input {
				outline: none;
				height: 40px;
				width: 250px;
				border-radius: 20px;
				border: 1px solid #878787;
				background-color: rgba(255, 255, 255, 0.66);
				text-indent: 20px;
				font-size: 18px;
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

			.el-button {
				background-color: rgba(255, 255, 255, 0.66);
				color: #616161;

				&:hover {
					background-color: rgba(255, 255, 255, 0.9);
				}
			}
		}
	}

	.first {
		position: absolute;
		width: 400px;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}

	.second {
		position: absolute;
		width: 400px;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);

		.email-input {
			margin-top: 20px;
		}

		.tip {
			margin: 20px 0 0;
			text-align: center;
			display: block;

			.el-button {
				background-color: rgba(255, 255, 255, 0.66);
				color: #616161;

				&:hover {
					background-color: rgba(255, 255, 255, 0.9);
				}

				.timeout {
					display: inline-block;
					animation: time 1s infinite both;
					margin-right: 5px;
					color: #078595;
				}

				@keyframes time {
					0% {
						transform: scale(1);
						opacity: 1;
					}
					50% {
						transform: scale(1.25);
					}
					100% {
						transform: scale(1.5);
						opacity: 0;
					}
				}

			}
		}

	}

	.third{
		position: absolute;
		width: 435px;
		height: 235px;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		border: 1px solid rgba(255, 215, 0, 0.4);
		border-radius: 30px;
		display: flex;
		justify-content: center;
		flex-direction: column;
		align-items: center;
		h1{
			font-size: 25px;
		}
		h2{
			text-align: center;
			font-size: 20px;
		}

	}
}

.slide-in-right {
	animation: slide-in-right 0.5s ease both;
}

@keyframes slide-in-right {
	0% {
		transform: translate(150%, -50%);
		opacity: 0;
	}
	100% {
		transform: translate(-50%, -50%);
		opacity: 1;
	}
}

.slide-out-left {
	animation: slide-out-left 0.2s linear both;
}

@keyframes slide-out-left {
	0% {
		transform: translate(-50%, -50%);
		opacity: 1;
	}
	100% {
		transform: translate(-150%, -50%);
		opacity: 0;
	}
}

.onError{
	border: 1px solid red;
	animation: shake-horizontal 0.6s cubic-bezier(0.455, 0.030, 0.515, 0.955) both;
}

@keyframes shake-horizontal {
	0%,
	100% {
		transform: translateX(0);
	}
	10%,
	30%,
	50%,
	70% {
		transform: translateX(-10px);
	}
	20%,
	40%,
	60% {
		transform: translateX(10px);
	}
	80% {
		transform: translateX(8px);
	}
	90% {
		transform: translateX(-8px);
	}
}

</style>