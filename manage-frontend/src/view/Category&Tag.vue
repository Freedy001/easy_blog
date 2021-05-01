<!--suppress ALL -->
<template>
<div class="index-container">
	<div class="cardrow">
		<el-card class="box-card">
			<h1>分类</h1>
			<br>
			<el-form ref="form" :model="form">
				<el-form-item label="名称">
					<el-input type="text"
					          v-model="form.name"
					          placeholder="请输入标签名称"
					          maxlength="10"
					          show-word-limit
					></el-input>
				</el-form-item>
				<el-form-item label="图片url">
					<el-input type="text"
					          v-model="form.name"
					          placeholder="请输入图片url"
					></el-input>
				</el-form-item>
				<el-form-item label="优先级">
					<el-select v-model="form.region" placeholder="请选择标签优先级">
						<el-option label="最高" value="1"></el-option>
						<el-option label="高" value="2"></el-option>
						<el-option label="普通" value="3"></el-option>
						<el-option label="低" value="4"></el-option>
						<el-option label="最低" value="5"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<div class="button-area">
				<div>
					<el-button type="primary" round>添加</el-button>
					<el-button round>返回</el-button>
				</div>
				<el-button type="danger" round>删除</el-button>
			</div>
		</el-card>
		<el-card class="box-card">
			<h1>所有分类</h1>
			<br>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
		</el-card>
	</div>
	<div class="cardrow">
		<el-card class="box-card">
			<h1>标签</h1>
			<br>
			<el-form ref="form" :model="form">
				<el-form-item label="名称">
					<el-input type="text"
					          v-model="form.name"
					          placeholder="请输入标签名称"
					          maxlength="10"
					          show-word-limit
					></el-input>
				</el-form-item>
				<el-form-item label="图片url">
					<el-input type="text"
					          v-model="form.name"
					          placeholder="请输入图片url"
					></el-input>
				</el-form-item>
				<el-form-item label="优先级">
					<el-select v-model="form.region" placeholder="请选择标签优先级">
						<el-option label="最高" value="1"></el-option>
						<el-option label="高" value="2"></el-option>
						<el-option label="普通" value="3"></el-option>
						<el-option label="低" value="4"></el-option>
						<el-option label="最低" value="5"></el-option>
					</el-select>
				</el-form-item>
				<div class="button-area">
					<div>
						<el-button type="primary" round>添加</el-button>
						<el-button round>返回</el-button>
					</div>
					<el-button type="danger" round>删除</el-button>
				</div>
			</el-form>
		</el-card>
		<el-card ref="tag" class="box-card">
			<h1>所有标签</h1>
			<br>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag><el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
			<el-check-tag @change="onChange" :checked="checked">标签一</el-check-tag>
		</el-card>
	</div>
</div>
</template>

<script setup lang="ts">
import {getCurrentInstance, onMounted, reactive, ref} from "vue";
let {porxy} = getCurrentInstance();
let form = reactive({
	name: '',
	region: '',
	date1: '',
	date2: '',
	delivery: false,
	type: [],
	resource: '',
	desc: ''
})

function onSubmit() {
	console.log('submit!');
}

let checked=ref(true)
function onChange() {
	checked.value=!checked.value
}
//滚动


</script>

<style scoped lang="scss">
.index-container{
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	.cardrow {
		display: flex;
		justify-content: space-between;
		height: 45%;
	}
}

.box-card {
	width: 49%;
	overflow: auto;
	h1{
		color: #0b9aff;
		font-size: 20px;
		font-weight: lighter;
	}
	.el-form {
		padding: auto 0;
		.el-form-item {
			display: flex;
			justify-content: space-between;
			::v-deep(.el-form-item__label) {
				padding: 0;
				width: 20px;
				flex-grow: 1;
				text-align: left;
			}

			::v-deep(.el-form-item__content) {
				flex-grow: 1;
				width: 300px;
			}
		}
	}
	.el-check-tag{
		margin: 5px;
	}
	.button-area{
		display: flex;
		justify-content: space-between;
	}
}
</style>