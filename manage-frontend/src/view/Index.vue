<template>
	<div class="main">
		<Sidebar class="side"></Sidebar>
		<div class="container">
			<div class="content" id="content">
				<router-view v-slot="{ Component }">
					<transition enter-active-class="fade-in"
					            leave-active-class="fade-out"
					            mode="out-in">
						<component :is="Component"/>
					</transition>
				</router-view>
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
.fade-in {
	animation: fade-in 0.3s both;
}

@keyframes fade-in {
	0% {
		opacity: 0;
	}
	100% {
		opacity: 1;
	}
}

.fade-out {
	animation: fade-out 0.3s ease-out both;
}

@keyframes fade-out {
	0% {
		opacity: 1;
	}
	100% {
		opacity: 0;
	}
}


.main {
	display: flex;
	width: 100%;
	height: 100%;
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
		border-top-left-radius: 20px;
		border-bottom-left-radius: 20px;
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

			:deep .el-pagination {
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
