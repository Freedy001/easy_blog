<template>
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
					<span class="dot" :style="{'background-color':scope.row.dotColor}"></span>
					<span class="test">{{ scope.row.articleStatus }}</span>
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
				<el-button type="success" size="mini" @click="doSetting(scope.row.id)" round>设置</el-button>
				<el-button type="danger" size="mini" @click="doDel(scope.row.id)" round>删除</el-button>
			</template>
		</el-table-column>
	</el-table>
	<ArticleSettingDrawer :id="articleId"
	                      :isOpenDrawer="drawer"
	                      @saveCallback="save"
	                      @content="getContent">

	</ArticleSettingDrawer>
</template>

<script setup lang="ts">
import {ref, reactive, onMounted, getCurrentInstance, defineComponent} from "vue";
import {get, post} from "../http";
import {useRouter} from "vue-router";
import ArticleSettingDrawer from '../components/ArticleSettingDrawer.vue'
defineComponent({
	ArticleSettingDrawer
})
const {proxy} = getCurrentInstance();
const router = useRouter();
interface formData {
	id: string
	title: string,
	articleStatus: string,
	articleCategory: string,
	articleTags: Array<string>,
	articleDesc: string,
	commentNum: number,
	visitNum: number,
	likeNum: number,
	authorName: number,
	publishTime: string,
	updateTime: string,
	dotColor:string
}
let tableDate = reactive<Array<formData>>([])
onMounted(async () => {
	getData().then();
})
let page=ref(1)
async function getData(pageNum){
	let response;
	if (pageNum){
		response = await get(`/article/list?page=1&limit=${pageNum*20}`)
	}else {
		response = await get(`/article/list?page=${page.value}&limit=20`)
	}
	if (response.code == 200) {
		const arr: Array<formData> = response.data.list
		arr.forEach((value, index) => {
			//文章状态 0:未发布 1:已发布 2:顶置 3:推荐 4:回收站
			switch (value.articleStatus) {
				case '0':
					value.dotColor = '#fdf000'
					value.articleStatus = '未发布'
					break;
				case '1':
					value.dotColor = '#52c41a'
					value.articleStatus = '已发布'
					break;
				case '2':
					value.dotColor = '#125ee3'
					value.articleStatus = '顶置'
					break;
				case '3':
					value.dotColor = '#10fcf5'
					value.articleStatus = '推荐'
					break;
				case '4':
					value.dotColor = '#f6074e'
					value.articleStatus = '回收站'
					break;
			}
			tableDate.push(value)
		})
	} else {
		proxy.$notify.error({
			title: '错误',
			message: response.msg
		})
	}
}

//点击编辑按钮 跳转到文章页面
function doEdit(id) {
	router.push({
		path: '/index/article',
		query: {id: id}
	})
}
//设置回调
let articleId=ref<string>();
let drawer=ref(false)
function doSetting(id) {
	articleId.value=id
	drawer.value=!drawer.value
}

/**
 * 更改文章设置
 * @param form
 */
async function save(form){
	let existedTags: Array<number> = []
	let notExistedTag: Array<string> = []
	form.tagValue.forEach((value:any, index:number) => {
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
			message: '添加成功!',
			type: 'success'
		})
		tableDate.length=0;
		getData(page.value).then()
	} else {
		proxy.$notify.error({
			title: '错误',
			message: `添加失败！ reason-->${response.msg}`
		})
	}
}

async function doDel(id) {
	const response =await get(`/article/delete?ids=${id}`);
	if (response.code == 200) {
		proxy.$notify({
			title: '成功',
			message: '添加成功!',
			type: 'success'
		})
		tableDate.length=0;
		getData(page.value).then()
	} else {
		proxy.$notify.error({
			title: '错误',
			message: `添加失败！ reason-->${response.msg}`
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
</style>
