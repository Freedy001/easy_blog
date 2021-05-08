<template>
	<div class="card-row first">
		<el-card class="box-card" shadow="hover">
			<h1>文章</h1>
			<div>2</div>
		</el-card>
		<el-card class="box-card" shadow="hover">
			<h1>评论</h1>
			<div>2</div>
		</el-card>
		<el-card class="box-card" shadow="hover">
			<h1>阅读量</h1>
			<div>2</div>
		</el-card>
		<el-card class="box-card" shadow="hover">
			<h1>建立天数</h1>
			<div>2</div>
		</el-card>

	</div>
	<div class="card-row second">
		<el-card class="box-card" shadow="hover">
			<h1>动态</h1>
		</el-card>
		<el-card class="box-card" shadow="hover">
			<h1>操作记录</h1>
		</el-card>
	</div>
</template>

<script setup lang="ts">

</script>

<style scoped lang="scss">
.card-row{
	margin: 20px;
	display: flex;
	justify-content: space-around;
	flex-wrap: wrap;
	.box-card{
		width: 22%;
		h1{
			font-weight: lighter;
			font-size: 20px;
			margin-bottom: 20px;
		}
		div{
			font-weight: bold;
			font-size: 25px;
		}
	}
}
.card-row.second{
	.box-card{
		overflow: auto;
		width: 48%;
		max-height: 500px;
	}
}

</style>