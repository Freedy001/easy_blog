<template>
	<router-view></router-view>
</template>

<script setup lang="ts">

</script>

<style lang="scss">
*{
	margin: 0;
	padding: 0;
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
	margin: 0;
	padding: 0;
}
#app{
	height: 100%;
	width: 100%;
}
.icon {
	width: 1em;
	height: 1em;
	vertical-align: -0.15em;
	fill: currentColor;
	overflow: hidden;
}
</style>