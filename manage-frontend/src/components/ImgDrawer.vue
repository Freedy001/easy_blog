<template>
	<el-drawer
			title="选择图片"
			:size="500"
			v-model="drawer"
			destroy-on-close>
		<div class="container">
			<div>
				<div class="search">
					<el-input v-model="input" placeholder="搜索"></el-input>
					<el-button type="primary" icon="el-icon-search"></el-button>
				</div>
				<div class="pic-area">
					<div class="img-container"  v-for="i in 2">
						<el-image class="image"
						          fit="fill"
						          lazy
						          src="https://cdn.pixabay.com/photo/2020/07/04/06/41/clouds-5368444_1280.jpg"
						></el-image>
					</div>
				</div>
			</div>
			<div class="btn-area">
				<el-divider></el-divider>
				<el-button type="danger">关闭</el-button>
				<el-button type="primary">上传一个</el-button>
			</div>
		</div>
	</el-drawer>
	<teleport to="body">
		<div class="full-screen">
			<el-upload
					class="upload-demo"
					drag
					action="http://localhost:1000/upload"
					list-type="picture"
					:headers="token"
					multiple>
				<i class="el-icon-upload"></i>
				<div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
			</el-upload>
		</div>
	</teleport>
</template>

<script setup lang="ts">
import {ref} from "vue";

let drawer = ref(true)
let input = ref('')

let token = {Authorization:localStorage.getItem('Authorization')}


</script>

<style scoped lang="scss">
.container {
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	.search {
		margin: 20px;
		display: flex;
	}
	.pic-area {
		margin: 10px;
		overflow: auto;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		.img-container{
			width: 48%;
			.image{
				width: 100%;
				height: auto;
			}
		}
	}
	.btn-area{
		text-align: right;
		margin: 5px;
		.el-divider{
			margin: 5px;
		}
	}

}

.full-screen {
	width: 100%;
	height: 100%;
	position: absolute;
	top: 0;
	left: 0;
	z-index: 1000;

	.upload-demo {
		position: absolute;
		top: 15%;
		left: 52%;
		transform: translate(-50%);
		width: 30%;

	}

	background-color: rgba(0, 0, 0, 0.8);
}

</style>