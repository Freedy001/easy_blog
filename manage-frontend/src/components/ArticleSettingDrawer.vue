<template>
	<el-drawer
			:size="500"
			title="发布文章"
			v-model="drawer"
			direction="rtl"
			destroy-on-close>
		<div class="drawer-container">
			<h1>基本设置</h1>
			<div class="item">
				<p>标题名称</p>
				<el-input placeholder="请输入标题" v-model="form.title"></el-input>
			</div>
			<div class="item">
				<p>发布时间</p>
				<el-date-picker
						v-model="form.publishTime"
						style="width: 100%;"
						type="datetime"
						placeholder="选择日期时间">
				</el-date-picker>
			</div>
			<div class="item">
				<p>开启评论</p>
				<el-radio-group v-model="form.isComment">
					<el-radio :label="true">开启</el-radio>
					<el-radio :label="false">关闭</el-radio>
				</el-radio-group>
			</div>
			<div class="item">
				<p>是否顶置</p>
				<el-radio-group v-model="form.isOverhead">
					<el-radio :label="true">是</el-radio>
					<el-radio :label="false">否</el-radio>
				</el-radio-group>
			</div>
			<el-divider></el-divider>
			<div class="category-header">
				<h1>分类</h1>
				<el-button @click="addCategory" size="mini" round>新加一个</el-button>
			</div>
			<el-radio-group v-model="form.category">
				<el-radio
						v-for="(item,i) in categoryArr"
						:key="i"
						:label="item.id">
					{{ item.name }}
				</el-radio>
			</el-radio-group>
			<el-divider style="cursor: pointer" @click="addMore">
				<i class="el-icon-sunny"></i>
				<span>加载更多</span>
			</el-divider>
			<h1>标签</h1>
			<el-select
					v-model="form.tagValue"
					multiple
					filterable
					allow-create
					default-first-option
					remote
					:remote-method="remoteMethod"
					:loading="loading"
					placeholder="请选择文章标签">
				<el-option
						v-for="item in options"
						:key="item.value"
						:label="item.label"
						:value="item.value">
				</el-option>
			</el-select>
			<el-divider></el-divider>
			<h1>摘要</h1>
			<el-input type="textarea" placeholder="请输入文章的摘要!" show-word-limit maxlength="300" v-model="form.desc"></el-input>
			<el-divider></el-divider>
			<h1>封面图</h1>
			<div @click="innerDrawer++" class="poster">
				<el-image
						fit="fill"
						:src="loadResource(form.url)"
				>
					<template #error>
						<div class="image-slot">
							<span>点我,选择一个图片吧🤪</span>
						</div>
					</template>
					<template #placeholder>
						<div class="image-slot">
							加载中<span class="dot">...</span>
						</div>
					</template>
				</el-image>
			</div>
			<el-input placeholder="点击封面选择图片，或者输入外部链接" v-model="form.url"></el-input>
			<el-divider></el-divider>
			<div>
				<el-button type="primary" @click="onSave">保存</el-button>
				<el-button @click="drawer=false">取消</el-button>
			</div>
		</div>
		<ImgDrawer :isDrawer="innerDrawer" @clickCallback="clickCb"></ImgDrawer>
	</el-drawer>
	<transition name="el-fade-in">
		<FullScreen v-if="showCard" @clickOutSide="showCard=false" :opacity="0.2">
			<div class="boxs">
				<CategoryOrTagCard @callback="addSuccess" name="分类"></CategoryOrTagCard>
			</div>
		</FullScreen>
	</transition>
</template>

<script setup lang="ts">
import {
	defineComponent,
	defineEmit,
	defineProps,
	getCurrentInstance,
	onActivated,
	onMounted,
	reactive,
	ref,
	watch
} from "vue";
import {get, loadResource} from "../http";
import {ElMessage} from "element-plus";
import ImgDrawer from './ImgDrawer.vue'
import CategoryOrTagCard from './CategoryOrTagCard.vue'
import FullScreen from './FullScreen.vue'
import {useStore} from "vuex";
import {noPermission} from "../util/Common";
defineProps(['id','status', 'isOpenDrawer'])
defineEmit(['saveCallback', 'content'])
defineComponent({
	ImgDrawer,
	CategoryOrTagCard,
	FullScreen
})

const {proxy}: any = getCurrentInstance();
const store = useStore();
//是否打开图片侧边栏
let innerDrawer = ref(0)
//显示添加分类
let showCard = ref(false)
//表单的所有数据
let drawer = ref(false)
watch(() => proxy.isOpenDrawer, (val) => {
	drawer.value = true;
})
//图片回显
function clickCb(url: string) {
	form.url = url;
}

interface originalForm {
	title: string,
	publishTime: Date,
	isComment: boolean,
	isOverhead: boolean,
	category: number,
	authorId: number,
	desc: string,
	tagValue: Array<any>,
	url: string|null,
	articleStatus:any
}

//抽屉中表单的数据项
let form = reactive<originalForm>({
	title: '',
	publishTime: new Date(),
	isComment: true,
	isOverhead: false,
	authorId: 0,
	category: 0,
	desc: '',
	tagValue: [],
	url: null,
	articleStatus:null
})
watch(() => form.title, (val) => {
	store.commit('setTitle', val)
})
watch(() => store.state.articleTitle, (val) => {
	form.title = val
})
let options = reactive([{
	value: '',
	label: ''
}])
let loading=ref(false)
//获取建议
async function remoteMethod(queryString:string) {
	loading.value=true;
	const response =await get(`/tag/getSuggestion?queryString=${queryString}`);
	if (response.code==200){
		options.length=0;
		response.data.forEach((value: any)=>{
			options.push({
				value: value.id,
				label: value.tagName
			})
		})
	}
	loading.value=false;
}

//分类数组
let categoryArr = reactive<any>([]);

//增加分类
function addCategory() {
	const el:any = document.querySelector('.el-overlay');
	el.style.zIndex=10
	showCard.value = true
}

let page = 1;

//添加分类成功后的回调
async function addSuccess() {
	//重新获取分类
	const data = await get(`/category/list?page=1&limit=${page * 6}&sidx=priority&order=asc`);
	if (data.code == 200) {
		let arr: Array<any> = data.page.list
		arr.forEach((value, index) => {
			categoryArr[index] = {
				id: value.id,
				name: value.categoryName,
				url: value.categoryImgUrl,
				priority: value.priority
			}
		})
	} else {
		ElMessage({
			showClose: true,
			message: `网络异常！${data.msg}`,
			type: 'error'
		});
	}
	showCard.value = false
}

//加载更多
async function addMore() {
	const data = await get(`/category/list?page=${++page}&limit=6&sidx=priority&order=asc`);
	if (data.code == 200) {
		let arr: Array<any> = data.page.list
		arr.forEach((value) => {
			categoryArr.push({
				id: value.id,
				name: value.categoryName,
				url: value.categoryImgUrl,
				priority: value.priority
			})
		})
	} else {
		ElMessage({
			showClose: true,
			message: `网络异常！${data.msg}`,
			type: 'error'
		});
	}
}

//点击保存 通知父组件进行保存
async function onSave() {
	proxy.$emit('saveCallback', form);
	drawer.value = false//关闭抽屉！
}

//******************************数据初始化******************************
onMounted(async () => {
	const el:any = document.querySelector('.el-drawer.rtl');
	el.style.overflow='auto'
	el.style.zIndex=10
	initDate().then();
})

watch(() => proxy.id, (val) => {
	if (val) {
		initDate();
	}
})

//后台传来的数据接口
interface formData {
	title: string,
	content: string,
	publishTime: Date,
	authorId: number,
	isComment: boolean,
	isOverhead: boolean,
	articleCategoryId: number,
	articleDesc: string,
	articlePoster: string,
	existedTags: Array<any>,
	notExistedTag: Array<string>,
}

//初始化数据
async function initDate() {
	//获取分类
	const data = await get(`/category/list?page=${page}&limit=6&sidx=priority&order=asc`);
	if (data.code == 200) {
		let arr: Array<any> = data.page.list
		arr.forEach((value, index) => {
			categoryArr[index] = {
				id: value.id,
				name: value.categoryName,
				priority: value.priority
			}
		})
	} else {
		ElMessage({
			showClose: true,
			message: `网络异常！${data.msg}`,
			type: 'error'
		});
	}
	//获取标签
	const tegRes = await get(`/tag/list?page=${page}&limit=20&sidx=priority&order=asc`);
	if (tegRes.code == 200) {
		let arr: Array<any> = tegRes.page.list
		arr.forEach((value, index) => {
			options[index] = {
				value: value.id,
				label: value.tagName
			}
		})
	} else {
		ElMessage({
			showClose: true,
			message: `网络异常！${data.msg}`,
			type: 'error'
		});
	}
	//当传入id时 进行数据回显
	if (proxy.id) {
		const data = await get(`/article/info/${proxy.id}`);
		if (data.code==200){
			const info: formData = data.data;
			form.title = info.title
			form.publishTime = (new Date(info.publishTime))
			form.isComment = info.isComment
			form.isOverhead = info.isOverhead
			form.category = info.articleCategoryId
			form.desc = info.articleDesc
			form.tagValue = info.existedTags
			form.url = info.articlePoster
			form.authorId = info.authorId
			form.articleStatus = proxy.status;
			proxy.$emit('content', info.content);
		}else {
			noPermission()
		}
	}
}

</script>

<style scoped lang="scss">
.drawer-container {
	margin: 20px;

	.item {
		margin: 20px 0;

		p {
			margin: 10px 0;
		}
	}

	h1 {
		margin: 10px 0;
		font-size: 20px;
	}

	.el-image {
		width: 100%;
		height: 100%;
		min-height: 200px;
		cursor: pointer;
		border: 1px solid #46b6f3;
		border-radius: 10px;
	}

	.image-slot {
		width: 100%;
		height: 180px;
		display: flex;
		justify-content: center;
		align-items: center;

		span {
			color: #929292;
		}
	}

	.category-header {
		display: flex;
		justify-content: space-between;

		.el-button {
			height: 35px;
		}

	}

	.el-radio-group {
		margin: 10px 0;
		display: flex;
		flex-wrap: wrap;

		.el-radio {
			margin: 5px;
			width: 25%;
		}
	}

	.el-select {
		width: 100%;
	}

}

:deep(.el-textarea__inner) {
	height: 200px;
}

.full-screen {
	.boxs {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		height: 400px;

		.card-row {
			height: 100%;
			width: 500px;
		}
	}

	background-color: rgba(0, 0, 0, 0.9);
}

:deep(.tag-area) {
	display: none;
}


:deep(#box-card) {
	width: 100%;
}

</style>