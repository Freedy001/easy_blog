<template>
	<div class="main">
		<Sidebar class="side"></Sidebar>
		<div class="container">
			<div class="content">
				<router-view></router-view>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import {defineComponent} from 'vue'
import Sidebar from "../components/Sidebar.vue";

defineComponent({
	Sidebar
})


</script>

<style scoped lang="scss">

.main {
	display: flex;
	width: 90%;
	height: 85%;
	//max-height: 90vh;
	//max-width: 1200px;
	border-radius: 20px;
	margin: auto;
	overflow: hidden;
	background: #0e8bff;

	.side {

	}

	.container {
		flex: 1;
		width: 100%;
		height: 100%;
		padding: 40px 0;
		border-radius: 20px;
		background: #fff;
		overflow: hidden;
		transition: all .3s;
		position: relative;

		.content {
			width: 100%;
			height: 100%;
			padding: 0 50px;
			overflow: auto;

			&::-webkit-scrollbar-track {
				background: #fff;
			}

			&::-webkit-scrollbar-thumb {
				background: #eef7ff;
			}

			&::-webkit-scrollbar {
				width: 6px;
				height: 6px;
			}

			::v-deep .el-pagination {
				position: absolute;
				bottom: 20px;
				left: 0;
				right: 0;
				background: white;
				z-index: 9;
				padding: 15px 0;
				text-align: center;
				font-weight: 400;
			}
		}
	}
}

</style>
