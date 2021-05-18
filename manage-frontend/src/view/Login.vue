<template>
	<div class="outer">
		<div class="form">
			<h2>Welcome Home!</h2>
			<div class="form-item">
				<div class="ipt user">
					<input type="text" placeholder="Name" v-model="username">
					<img src="../assets/login-1.png" alt="">
				</div>
				<div class="ipt pass">
					<input type="password" placeholder="Password" @keydown.enter="loginBtn" v-model="password">
					<img src="../assets/login-2.png" alt="">
				</div>
				<img src="../assets/login-0.png" alt="">
				<el-button type="primary" @click="loginBtn">sign in</el-button>
				<p class="options">
					<span>- SignIn</span>
					or
					<span>Password -</span>
				</p>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, ref} from 'vue'
import {login} from "../http"
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";

export default defineComponent({
	name: "Login.vue",
	setup() {
		const {proxy}=getCurrentInstance()
		const username = ref<string>("")
		const password = ref<string>("")
		const router = useRouter()
		const loginBtn=async ()=>{
			const boolean = await login({
				"username":username.value,
				"password":password.value
			});
			if (boolean){
				await router.push("/index")
			}else {
				const id=setInterval(()=>{
					proxy.$notify.error({
						title: '出差啦😢！',
						message: '亲，密码错了哦！',
					});
					proxy.$notify.error({
						title: '出差啦😢！',
						message: '亲，密码错了哦！',
						position: 'top-left'
					});
				},50)
				setTimeout(()=>{
					clearInterval(id)
				},1000)
			}
		}

		return {
			username,
			password,
			loginBtn
		}
	}
})
</script>

<style scoped lang="scss">
.outer{
	position: absolute;
	width: 100%;
	height: 100%;
}
.form{
	position: absolute;
	left: 50%;
	top: 50%;
	width: 380px;
	padding: 40px 0 24px;
	background: #fff;
	border-radius: 10px;
	text-align: center;
	transform: translate(-50%, -50%);
	box-shadow: 0 2px 10px #e6e6e6;
	z-index: 9;
	.form-item{
		padding: 0 50px;
		div img{
			display: none;
		}
		.ipt:focus-within{
			& ~ img{
				display: none;
			}
			img{
				display: block;
			}
			&.user img{
				width: 120px;
				height: 113px;
			}
			&.pass img{
				width: 103px;
				height: 84px;
				top: -60px;
			}
		}
		img{
			position: absolute;
			top: -80px;
			left: 50%;
			width: 120px;
			height: 95px;
			transform: translate(-50%, 0);
		}
		.options{
			color: #ccc;
			span{
				font-size: 12px;
				display: inline-block;
				margin-top: 6px;
				cursor: pointer;
			}
		}
	}
}
input{
	width: 100%;
	height: 44px;
	border-radius: 4px;
	border: 1px solid #eee;
	margin-bottom: 12px;
	font-size: 14px;
	padding: 0 12px;
	color: #606060;
	transition: all .3s;
	outline: none;
	&:hover{
		border-color: #0b9aff;
	}
	&:focus-within + img{
		display: block;
	}
}
button {
	border: none;
	background: #0b9aff;
}
</style>