<template>
	<div class="indexContainer">
		<div class="card-row">
			<el-card class="box-card card1" shadow="hover">
				<i class="el-icon-magic-stick"></i>
				<span>首页效果</span>
			</el-card>
			<el-card class="box-card card2" shadow="hover">
				<i class="el-icon-magic-stick"></i>
				<span>常规设置</span>
			</el-card>
		</div>
		<div class="card-row">
			<el-card class="box-card card3" shadow="hover">
				<i class="el-icon-magic-stick"></i>
				<span>附件设置</span>
			</el-card>
			<el-card class="box-card card4" shadow="hover">
				<i class="el-icon-magic-stick"></i>
				<span>SMTP服务</span>
			</el-card>
		</div>
	</div>
</template>

<script setup lang="ts">

</script>

<style scoped lang="scss">
.indexContainer {
	width: 100%;
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: center;

	.card-row {
		display: flex;
		justify-content: center;
		height: 40%;

		.box-card {
			width: 48%;
			margin: 10px;
			position: relative;

			i {
				position: absolute;
				font-size: 100px;
				top: 50%;
				left: 25%;
				transform: translate(-50%, -50%);
			}

			span {
				position: absolute;
				font-size: 50px;
				width: 250px;
				top: 50%;
				left: 65%;
				transform: translate(-50%, -50%);
			}
		}

		.card1 {
			background-color: #505050;
			&:hover {
				filter: blur(0);
			}
		}

		.card2 {

			&:hover {
				filter: blur(0);
			}
		}

		.card3 {

			&:hover {
				filter: blur(0);
			}
		}

		.card4 {
			background-color: #b2dcff;
			&:hover {
				filter: blur(0);
			}
		}
	}
}
</style>