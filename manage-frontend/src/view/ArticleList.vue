<template>
	<div class="root">
		<el-table
				class="article-table"
				:data="tableDate"
				style="width: 100%">
			<el-table-column type="expand">
				<template #default="scope">
					<el-form label-position="left" inline class="demo-table-expand">
						<el-form-item label="分类">
							<el-tag>{{ scope.row.articleCategory }}</el-tag>
						</el-form-item>
						<el-form-item label="标签">
							<el-tag v-for="item in scope.row.articleTags" type="success">{{ item }}</el-tag>
						</el-form-item>
						<el-form-item label="作者">
							<span>{{ scope.row.authorName }}</span>
						</el-form-item>
						<el-form-item label="发布时间">
							<span>{{ scope.row.publishTime }}</span>
						</el-form-item>
						<el-form-item label="访问数">
							<span>{{ scope.row.visitNum }}</span>
						</el-form-item>
						<el-form-item label="更新时间">
							<span>{{ scope.row.updateTime }}</span>
						</el-form-item>
						<el-form-item label="文章描述">
							<div style="width: 100%; height: auto;word-break: break-all;overflow: hidden;">
								{{ scope.row.articleDesc }}
							</div>
						</el-form-item>
					</el-form>
				</template>
			</el-table-column>
			<el-table-column
					label="标题"
					min-width="200px"
					prop="title">
			</el-table-column>
			<el-table-column
					label="状态">
				<template #default="scope">
					<div class="status">
						<span class="dot" :style="{'backgroundColor':scope.row.dotColor}"></span>
						<span class="test">{{ scope.row.status }}</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="点赞数">
				<template #default="scope">
				<span>
					{{ scope.row.likeNum }}
				</span>
				</template>
			</el-table-column>
			<el-table-column label="评论数">
				<template #default="scope">
				<span>
					{{ scope.row.commentNum }}
				</span>
				</template>
			</el-table-column>
			<el-table-column
					label="操作"
					min-width="250px"
			>
				<template #default="scope">
					<el-button type="primary" size="mini" @click="doEdit(scope.row.id)" round>编辑</el-button>
					<el-button type="success" size="mini" @click="doSetting(scope.row.id,scope.row.articleStatus)" round>设置
					</el-button>
					<el-button type="danger" size="mini" @click="doDel(scope.row.id)" round>删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
				small
				background
				layout="prev, pager, next"
				:page-count="totalPage"
				@current-change="changePage">
		</el-pagination>
		<ArticleSettingDrawer :id="articleId"
		                      :status="articleStatus"
		                      :isOpenDrawer="drawer"
		                      @saveCallback="save">

		</ArticleSettingDrawer>
	</div>
</template>

<script setup lang="ts">
import {ref, reactive, onMounted, getCurrentInstance, defineComponent, watch} from "vue";
import {get, post} from "../http";
import {useRouter} from "vue-router";
import ArticleSettingDrawer from '../components/ArticleSettingDrawer.vue'
import {useStore} from "vuex";

defineComponent({
	ArticleSettingDrawer
})
const {proxy}: any = getCurrentInstance();
const router = useRouter();
const store = useStore();

interface formData {
	id: string
	title: string,
	articleStatus: number | string,
	articleCategory: string,
	articleTags: Array<string>,
	articleDesc: string,
	commentNum: number,
	status: string | number,
	visitNum: number,
	likeNum: number,
	authorName: number,
	publishTime: string,
	updateTime: string,
	dotColor: string
}

let tableDate = reactive<Array<formData>>([])
onMounted(async () => {
	getData().then();
})
let page = 1
let totalPage = ref(1);

/**
 * 获取文章数据
 */
async function getData() {
	let response;
	response = await get(`/article/list?page=${page}&limit=16`)
	if (response.code == 200) {
		tableDate.length = 0
		totalPage.value = response.data.totalPage;
		const arr: Array<formData> = response.data.list
		arr.forEach((value, index) => {
			//文章状态 1:未发布 2:回收站 3:已发布 4:顶置 5:推荐
			switch (value.articleStatus) {
				case 1:
					value.dotColor = '#fdf000'
					value.status = '未发布'
					break;
				case 2:
					value.dotColor = '#f6074e'
					value.status = '回收站'
					break;
				case 3:
					value.dotColor = '#52c41a'
					value.status = '已发布'
					break;
				case 4:
					value.dotColor = '#10fcf5'
					value.status = '顶置'
					break;
				case 5:
					value.dotColor = '#125ee3'
					value.status = '推荐'
					break;
			}
			tableDate.push(value)
		})
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

function changePage(currentPage: number) {
	page = currentPage;
	getData()
}

//点击编辑按钮 跳转到文章页面
function doEdit(id: any) {
	if (store.state.articleContent!=='') {
		proxy.$confirm('你貌似还有文章没保存，直接编辑文章将会覆盖你原来的文章,你确定要继续吗?', '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		}).then(() => {
			router.push({
				path: '/index/article',
				query: {id: id}
			})
		})
	}else {
		router.push({
			path: '/index/article',
			query: {id: id}
		})
	}
}

//设置回调
let articleId = ref<string>();
let drawer = ref(false)
let articleStatus = ref<number | string>();

//设置文章
function doSetting(id: any, status: any) {
	articleStatus.value = status
	articleId.value = id
	drawer.value = !drawer.value
}

/**
 * 更改文章设置
 * @param form
 */
async function save(form: any) {
	let existedTags: Array<number> = []
	let notExistedTag: Array<string> = []
	form.tagValue.forEach((value: any, index: number) => {
		if ((typeof value) == 'number') {
			existedTags.push(value)
		} else if ((typeof value) == 'string') {
			notExistedTag.push(value)
		}
	})
	const response = await post(`/article/saveOrUpdate`, {
		id: articleId.value,
		title: form.title,
		publishTime: form.publishTime.getTime(),
		isComment: form.isComment,
		articleStatus: form.articleStatus,
		isOverhead: form.isOverhead,
		authorId: form.authorId,
		articleCategoryId: form.category,
		articleDesc: form.desc,
		articlePoster: form.url,
		existedTags: existedTags,
		notExistedTag: notExistedTag,
	});
	if (response.code == 200) {
		proxy.$notify({
			title: '成功',
			message: '保存成功!',
			type: 'success'
		})
		tableDate.length = 0;
		page = 1;
		getData().then()
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message:response.msg
		})
	}
}

/**
 * 删除文章
 * @param id
 */
async function doDel(id: any) {
	const response = await get(`/article/delete?ids=${id}`);
	if (response.code == 200) {
		proxy.$notify({
			title: '成功',
			message: '删除成功!',
			type: 'success'
		})
		tableDate.length = 0;
		page = 1;
		getData().then()
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

</script>

<style scoped lang="scss">
.status {
	display: flex;
	justify-content: start;
	align-items: center;

	.dot {
		width: 6px;
		height: 6px;
		border-radius: 50%;
		background-color: #52c41a;
		display: block;
		margin-right: 5px;
	}
}

.demo-table-expand {
	font-size: 0;
	display: flex;
	justify-content: start;
	flex-wrap: wrap;

	label {
		width: 90px;
		color: #99a9bf;
	}

	.el-form-item:nth-child(odd) {
		margin-right: 0;
		margin-bottom: 0;
		width: 35%;
	}

	.el-form-item:nth-child(even) {
		margin-right: 0;
		margin-bottom: 0;
		width: 60%;
	}

	.el-form-item:last-child {
		margin-right: 0;
		margin-bottom: 0;
		width: 100%;
		height: auto;
	}


	.el-tag {
		margin-left: 5px;
	}
}

:deep(.el-table__body-wrapper.is-scrolling-none) {
	margin-bottom: 50px;
}
</style>
