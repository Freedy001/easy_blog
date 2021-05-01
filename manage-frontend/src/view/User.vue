<template>
	<div class="index-container">
		<el-card class="box-card left">
			<div class="info">
				<div class="photo">
					<img src="src/assets/login-0.png" alt="">
				</div>
				<p class="name">Admin-Freedy</p>
			</div>
			<div class="introduce">
				<div class="item">
					<i class="el-icon-ice-cream-round"></i>
					<a href="#" style="color: #0b9aff;text-decoration: none">http://localhost:8090</a>
				</div>
				<div class="item">
					<i class="el-icon-ice-cream-square"></i>
					<p>985948228@qq.com</p>
				</div>
				<div class="item">
					<i class="el-icon-lollipop"></i>
					<p>62 天</p>
				</div>
			</div>
			<el-divider><i class="el-icon-edit"></i></el-divider>
			<div class="statistics">
				<p>累计发表了 1 篇文章。</p>
				<el-divider></el-divider>
				<p>累计创建了 3 个分类。</p>
				<el-divider></el-divider>
				<p>累计创建了 9 个标签。</p>
				<el-divider></el-divider>
				<p>累计获得了 1 条评论。</p>
				<el-divider></el-divider>
				<p>文章总阅读 8 次。</p>
				<el-divider></el-divider>

			</div>
		</el-card>

		<el-card class="box-card right">
			<h1>个人资料</h1>
			<el-tabs v-model="activeName" @tab-click="handleClick">
				<el-tab-pane label="基本资料" name="first">
					<el-form ref="form"
					         :model="form"
					>
						<el-form-item label="登录用户名">
							<el-input v-model="form.name"></el-input>
						</el-form-item>
						<el-form-item label="昵称">
							<el-input v-model="form.name"></el-input>
						</el-form-item>
						<el-form-item label="电子邮件">
							<el-input v-model="form.name"></el-input>
						</el-form-item>
						<el-form-item label="个人说明">
							<el-input type="textarea" v-model="form.desc"></el-input>
						</el-form-item>
						<el-form-item>
							<el-button type="primary" @click="onSubmit">立即创建</el-button>
							<el-button>取消</el-button>
						</el-form-item>
					</el-form>
				</el-tab-pane>
				<el-tab-pane label="修改密码" name="second">
					<el-form :model="ruleForm"
					         status-icon
					         :rules="rules"
					         class="demo-ruleForm">
						<el-form-item label="原密码" prop="oldPass">
							<el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
						</el-form-item>
						<el-form-item label="新密码" prop="pass">
							<el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
						</el-form-item>
						<el-form-item label="确认密码" prop="checkPass">
							<el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
						</el-form-item>
						<el-form-item>
							<el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
							<el-button @click="resetForm">重置</el-button>
						</el-form-item>
					</el-form>
				</el-tab-pane>
				<el-tab-pane label="用户管理" name="third">

				</el-tab-pane>
			</el-tabs>
		</el-card>
	</div>
</template>

<script setup lang="ts">
import {reactive, ref, watch} from "vue";

let activeName = ref('first')

function handleClick(tab, event) {
	console.log(tab, event);
}

let form = reactive(
		{
			name: '',
			region: '',
			date1: '',
			date2: '',
			delivery: false,
			type: [],
			resource: '',
			desc: ''
		})
//************************密码验证表单************************
let ruleForm = reactive({
	oldPass:'',
	pass: '',
	checkPass: ''
})
let rules = reactive({
	oldPass:[
		{validator: validatePass, trigger: 'blur'}
	],
	pass: [
		{validator: validatePass, trigger: 'blur'}
	],
	checkPass: [
		{validator: validatePass2, trigger: 'blur'}
	]
})

function validatePass(rule, value, callback) {
	if (value != '') {
		callback()
	}else {
		callback(new Error("请输入密码！"))
	}
}

function validatePass2(rule, value, callback) {
	if (ruleForm.pass == ruleForm.checkPass) {
		callback();
	} else {
		callback(new Error('两次输入密码不一致!'));
	}
}
function resetForm() {
	ruleForm.pass='';
	ruleForm.checkPass='';
}

</script>

<style scoped lang="scss">
.index-container {
	display: flex;
	width: 100%;
	height: 100%;
	justify-content: space-between;

	.box-card.left {
		margin: auto 0;
		width: 48%;
		height: 80%;
		display: flex;
		flex-direction: column;
		justify-content: center;

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
			margin-top: 15px;
			color: #000000;
			text-align: center;
			font-size: 25px;
		}

		.introduce {
			margin: 20px;

			.item {
				margin: 10px;

				p {
					display: inline;
				}
			}
		}

		.statistics {
			padding-top: 20px;
			margin: 20px;

			.el-divider {
				margin: 15px;
			}
		}
	}

	.box-card.right {
		margin: auto 0;
		width: 48%;
		height: 80%;

		h1 {
			font-size: 20px;
			font-weight: lighter;
		}

		::v-deep(#pane-first .el-form-item) {
			margin-bottom: 0;
		}

		::v-deep(.el-textarea__inner) {
			height: 180px;
		}


	}
}
</style>