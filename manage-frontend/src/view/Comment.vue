<template>
	<el-table
			class="article-table"
			:data="tableDate"
			style="width: 100%">
		<el-table-column type="expand">
			<template #default="props">
				<el-form label-position="left" inline class="demo-table-expand">
					<el-form-item label="评论人邮箱">
						<span>{{ props.row.email }}</span>
					</el-form-item>
					<el-form-item label="评论人IP">
						<span>{{ props.row.ip }}</span>
					</el-form-item>
					<el-form-item label="评论人地区">
						<span>{{ props.row.region }}</span>
					</el-form-item>
					<el-form-item label="父评论评论类容" v-if="props.row.fatherComment!==null">
						<span>{{ props.row.fatherComment }}</span>
					</el-form-item>
				</el-form>
			</template>
		</el-table-column>
		<el-table-column
				type="selection"
				width="55">
		</el-table-column>
		<el-table-column
				prop="username"
				label="昵称"
				width="100px"
		>
		</el-table-column>
		<el-table-column
				label="类容"
				prop="content"
		>
		</el-table-column>
		<el-table-column label="评论文章" width="200px">
			<template #default="scope">
				<a :href="`http://localhost:5000/#/article?id=${scope.row.article.id}`"
				   style="color: #0b9aff;text-decoration: none">{{ scope.row.article.title }}</a>
			</template>
		</el-table-column>
		<el-table-column
				label="发表日期"
				prop="createTime"
				width="200px"
		>
		</el-table-column>
		<el-table-column
				label="操作"
				width="80px"
		>
			<template #default="scope">
				<div class="operator">
					<el-tooltip class="item" effect="dark" content="回复" placement="top">
						<img :src="answerLogo" alt="" class="answer">
					</el-tooltip>
					<el-tooltip class="item" effect="dark" content="删除" placement="top">
						<img :src="deleteLogo" alt="" class="del">
					</el-tooltip>
				</div>
			</template>
		</el-table-column>
	</el-table>
</template>

<script setup lang="ts">
import {ref, reactive, onMounted, watch} from "vue";
import {get} from "../http";
import answerLogo from '../assets/answer.svg'
import deleteLogo from '../assets/delete.svg'
import {useStore} from "vuex";
const store = useStore();
interface ITableData {
	username: string
	content: string
	article: {
		id: string
		title: string
	},
	email: string
	IP: string
	region: string
	fatherComment: string
	createTime: string
}
onMounted(() => {
	getComment()
})

let multipleSelection = reactive([])

let tableDate = reactive<Array<ITableData>>([])
let page = 1;
/**
 * 加载评论
 */
async function getComment() {
	const response = await get(`/comment/list?page=${page}&limit=20`);
	if (response.code == 200) {
		const data: Array<ITableData> = response.data.list;
		data.forEach((value, index) => {
			tableDate.push(value)
		})
	}
}
watch(()=>store.state.scrollCount,(val)=>{
	page++;
	getComment();
})



</script>

<style scoped lang="scss">
.demo-table-expand {
	font-size: 0;
}

.demo-table-expand label {
	width: 90px;
	color: #99a9bf;
}

.demo-table-expand .el-form-item {
	margin-right: 0;
	margin-bottom: 0;
	width: 50%;
}

.operator {
	display: flex;
	align-items: center;
	justify-content: space-around;

	.answer {
		width: 18px;
		height: 18px;
	}

	img {
		width: 20px;
		height: 20px;
	}
}
</style>
