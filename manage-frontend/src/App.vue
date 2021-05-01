<template>
	<router-view></router-view>
</template>

<script setup lang="ts">

</script>

<style lang="scss">
*{
	margin: 0;
	padding: 0;
	list-style: none;
	box-sizing: border-box;
	&::-webkit-scrollbar {
		/*滚动条整体样式*/
		width : 5px;  /*高宽分别对应横竖滚动条的尺寸*/
		height: 1px;
	}
	&::-webkit-scrollbar-thumb {
		/*滚动条里面小方块*/
		border-radius: 10px;
		box-shadow   : inset 0 0 5px rgba(0, 0, 0, 0.2);
		background   : #7abbfd;
	}
}
body{
	overflow: hidden;
}
#app{
	padding: 0 15px;
	height: 100vh;
	width: 100vw;
	display: flex;
	background: linear-gradient(30deg, #00ccff, #00b9ff, #3a9ff5, #1e8bfd);
	background-size:1400% 300%;
	animation:mymove 5s ease-in-out infinite alternate;

}
@keyframes mymove {
	0%{
		background-position: 0% 0%;
	}
	50%{
		background-position: 50% 100%;
	}
	100%{
		background-position: 100% 0%;
	}
}

h2.tit{
	border-left: 2px solid var(--color-main);
	padding-left: 16px;
	font-size: 18px;
	font-weight: 400;
	margin: 20px 0 30px;
	color: var(--color-main);
	display: flex;
	align-items: center;
	justify-content: space-between;
	.add{
		height: 30px;
		line-height: 32px;
		font-size: 13px;
		border-radius: 4px;
		padding: 0 12px;
		color: var(--color-main);
		cursor: pointer;
		background: #e8f4ff;
		cursor: pointer;
		&:hover{
			color: #fff;
			background: var(--color-main);
		}
		.el-icon-plus{
			font-size: 12px;
			font-weight: 800;
		}
	}
	:root{
		--color-main: #0084ff;
		--color-main-hover: #0279e9;
	}
}
</style>