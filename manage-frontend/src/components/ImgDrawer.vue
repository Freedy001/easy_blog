<template>
	<el-drawer
			title="选择图片"
			:size="500"
			v-model="drawer"
			destroy-on-close>
		<div class="container">
			<div class="pic-index-container">
				<div class="search">
					<el-input v-model="input" placeholder="搜索"></el-input>
					<el-button type="primary" icon="el-icon-search"></el-button>
				</div>
				<div class="pic-area">
					<div class="img-container" v-for="(url,i) in resource" :key="i">
						<el-image class="image"
						          fit="cover"
						          :src="loadResource(url)"
						          @click="handClick(url)"
						          lazy
						></el-image>
					</div>
					<el-button @click="addMore" round style="width: 100%; margin-bottom: 30px">加载更多</el-button>
				</div>
			</div>
			<div class="btn-area">
				<el-divider></el-divider>
				<div style="margin: 5px;text-align: right">
					<el-button type="danger" @click="drawer=false">关闭</el-button>
					<el-button type="primary" @click="showCard=true">上传</el-button>
				</div>
			</div>
		</div>
	</el-drawer>
	<teleport to="body">
		<transition name="el-fade-in-linear">
			<Upload v-if="showCard" @click="showCard=false"></Upload>
		</transition>
	</teleport>
</template>

<script setup lang="ts">
import {
	defineComponent,
	defineEmit,
	defineProps,
	getCurrentInstance,
	onBeforeMount,
	onMounted,
	onUpdated,
	reactive,
	ref,
	watch
} from "vue";
import Upload from './Upload.vue'
import FullScreen from './FullScreen.vue'
import {ElMessage} from "element-plus";
import {get, loadResource} from "../http";

const {proxy}: any = getCurrentInstance();
let input = ref('')
let token = {Authorization: localStorage.getItem('Authorization')}
let showCard = ref(false)
let resource = reactive<Array<string>>([])
let page = 1
let drawer = ref(false);
defineProps(['isDrawer'])
defineEmit(['clickCallback'])
defineComponent({
	FullScreen,
	Upload
})
watch(() => proxy.isDrawer, (val) => {
	drawer.value = true;
})

function handClick(url: string) {
	proxy.$emit('clickCallback', url)
	drawer.value = false;
}

//关闭上传后重新加载
watch(showCard, (val) => {
	if (!val) {
		resource.length = 0
		page = 1;
		getImageUrls().then();
	}
})

function addMore() {
	page++;
	getImageUrls().then();
}


onMounted(async () => {
	getImageUrls().then()
})

//初始化图片预览
async function getImageUrls() {
	const response = await get(`/file/getImages?page=${page}&limit=10&sidx=id&order=desc`);
	if (response.code == 200) {
		const list: Array<any> = response.data.list
		list.forEach(value => resource.push(value.resourceUrl))
	}
}
</script>

<style scoped lang="scss">
.container {
	height: 100%;

	.pic-index-container {
		height: 100%;

		.search {
			margin: 20px;
			display: flex;
		}

		.pic-area {
			overflow: auto;
			max-height: 800px;
			margin: 10px;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;

			.img-container {
				cursor: pointer;
				margin: 10px;
				width: calc(100% / 2 - 20px);
				max-height: 250px;

				.el-image.image {
					width: 100%;
					height: 100%;
				}
			}
		}
	}

	.btn-area {
		position: absolute;
		left: 0;
		bottom: 0;
		width: 100%;
		background: white;

		.el-divider {
			margin: 5px;
		}
	}

}


</style>