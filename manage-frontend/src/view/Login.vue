<template style="background-color: #42b983">
		<img class="background" src="../assets/img.png" alt="."/>
	<div class="form">
		<h2>Welcome Home!</h2>
		<div class="form-item">
			<div class="ipt user">
				<input type="text" placeholder="Name" v-model="username">
				<img src="../assets/login-1.png">
			</div>
			<div class="ipt pass">
				<input type="password" placeholder="Password" v-model="password">
				<img src="../assets/login-2.png">
			</div>
			<img src="../assets/login-0.png" alt="">
			<el-button type="primary">sign in</el-button>
			<p class="options">
				<span>- SignUp</span>
				or
				<span>Password -</span>
			</p>
		</div>
	</div>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, ref} from 'vue'

export default defineComponent({
	name: "Login.vue",
	setup() {
		const username = ref()
		const password = ref()
		const {proxy}=getCurrentInstance()
		let {data} = proxy.$http.get('/app');
		console.log(data)

		return {
			username,
			password
		}
	}
})
</script>

<style scoped lang="scss">
.background{
	height: 100%;
	position: absolute;
	overflow: hidden;
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