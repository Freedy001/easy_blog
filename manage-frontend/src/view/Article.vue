<template>
	<div class="indexContainer">
		<div class="title-row">
			<h1>{{ input === '' ? '新文章' : input }}</h1>
			<div>
				<el-button type="danger">保存草稿</el-button>
				<el-button type="primary">发布</el-button>
			</div>
		</div>
		<el-input v-model="input" placeholder="请输入标题"></el-input>
	</div>
	<editor></editor>
</template>

<script setup lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import editor from '../components/MarkdownContainer.vue';

defineComponent({
	editor
})
let input = ref('请输入标题')


</script>

<style scoped lang="scss">
.indexContainer {
	.title-row {
		margin: 0 20px 10px 20px;
		display: flex;
		justify-content: space-between;
	}

	.el-input {
		padding: 10px 20px 10px 20px;
		width: 100%;
	}
}
</style>