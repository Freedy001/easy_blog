<!--suppress CssInvalidPseudoSelector -->
<template>
	<div class="indexContainer">
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="常规设置" name="first">
				<div class="common-setting">
					<div class="item">
						<h1>博客标题</h1>
						<el-input v-model="input" placeholder="请输入内容"></el-input>
					</div>
					<div class="logo-img item">
						<div class="logo">
							<h1>Logo</h1>
							<img :src="loadResource('/resource/EASY Blog White.svg')" alt="">
						</div>
						<div class="img">
							<h1>首页图片</h1>
							<img :src="loadResource('/resource/indexImg.jpg')" alt="">
						</div>
					</div>
					<div class="item color">
						<h1>首页色系</h1>
						<el-color-picker v-model="color" show-alpha class="color-picker"></el-color-picker>
						<div class="show-box">
							<div class="triangle" :style="triangle"></div>
							<img :src="loadResource('/resource/indexImg.jpg')" alt="">
						</div>
					</div>
					<div class="item">
						<h1>页脚信息</h1>
						<el-input
								type="textarea"
								:rows="2"
								placeholder="请输入内容"
								v-model="textarea">
						</el-input>
					</div>
					<el-button type="primary">保存</el-button>
				</div>
			</el-tab-pane>
			<el-tab-pane label="配置管理" name="second">

			</el-tab-pane>
			<el-tab-pane label="角色管理" name="third">

			</el-tab-pane>
			<el-tab-pane label="定时任务补偿" name="fourth">

			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script setup lang="ts">
import {reactive, ref, watch} from "vue";
import {loadResource} from "../http";

let activeName = ref('first')

function handleClick(tab: any, event: any) {

}

let textarea = ref('')
let color = ref('rgba(45, 37, 73, 0.7)')
let triangle = reactive({
	"border-bottom": "100vh solid rgba(45, 37, 73, 0.7)"
})
watch(color, () => {
	triangle["border-bottom"]=` 100vh solid ${color.value}`
})
</script>

<style scoped lang="scss">
.indexContainer {
	.common-setting {
		.item {
			margin: 30px 0;

			h1 {
				font-size: 25px;
				font-weight: normal;
				color: #535353;
				margin-bottom: 10px;
			}
		}

		.logo-img {
			display: flex;
			justify-content: space-around;

			img {
				border-radius: 20px;
				width: 300px;
				height: 300px;
			}
		}

		.color {
			position: relative;

			:deep(.color-picker) {
				width: 100%;
				height: 0;
				z-index: 99;
				opacity: 0;
			}

			:deep(.el-color-picker__trigger) {
				width: 100%;
				border: none;
				height: 0;
				padding-bottom: 66%;
				position: absolute;
				top: 0;
				left: 0;
			}

			.show-box {
				box-sizing: content-box;
				overflow: hidden;
				position: relative;
				width: 100%;
				height: 0;
				padding-bottom: 66%;
				border-radius: 20px;

				.triangle {
					position: absolute;
					top: 0;
					left: 0;
					border-right: 50vw solid rgba(36, 145, 191, 0);
					transform: translate(0, -30vh);
					transition: all 0.3s;
				}

				img {
					width: 100%;
				}
			}
		}

	}
}
</style>