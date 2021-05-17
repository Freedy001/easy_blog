<!--suppress JSUnresolvedVariable, JSUnresolvedFunction -->
<template>
	<div class="root">
		<div class="btn-area">
			<el-button type="primary" @click="batchConfirm" round>批量通过</el-button>
			<el-button type="danger" @click="batchDel" round>批量删除</el-button>
		</div>
		<el-table
				class="article-table"
				:data="tableDate"
				@selection-change="handleSelectionChange"
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
						<div v-if="scope.row.commentStatus===0">
							<el-tooltip class="item" effect="dark" content="审核通过" placement="top">
								<img :src="examine" alt="" class="examine" @click="doExamine(scope.row.id)">
							</el-tooltip>
						</div>
						<div v-else>
							<el-tooltip class="item" effect="dark" content="回复" placement="top">
								<img :src="answerLogo" alt="" class="answer"
								     @click="doAnswer(scope.row.id,scope.row.article.id,scope.row.username)">
							</el-tooltip>
						</div>
						<div>
							<el-tooltip class="item" effect="dark" content="删除" placement="top">
								<img :src="deleteLogo" alt="" class="del" @click="doDel(scope.row.id)">
							</el-tooltip>
						</div>
					</div>
				</template>
			</el-table-column>
		</el-table>
		<transition name="el-fade-in">
			<FullScreen opacity="0.1" v-if="showReplayCard">
				<div class="content">
					<div class="toWho">
						<span>回复: {{ replay.replayName }}</span>
						<img :src="close" alt="" @click="doClose">
					</div>
					<textarea placeholder="请输入内容" v-model="replay.content"></textarea>
					<div class="button">
						<el-button type="primary" round @click="doReplay">@回复他</el-button>
					</div>
				</div>
			</FullScreen>
		</transition>
	</div>
</template>

<script setup lang="ts">
import {ref, reactive, onMounted, watch, defineComponent, getCurrentInstance} from "vue";
import {get, post} from "../http";
import answerLogo from '../assets/answer.svg'
import deleteLogo from '../assets/delete.svg'
import examine from '../assets/examine.svg'
import close from '../assets/close.svg'
import FullScreen from '../components/FullScreen.vue'
import {useStore} from "vuex";

const {proxy}: any = getCurrentInstance();
defineComponent({
	FullScreen,
})
const store = useStore();

interface ITableData {
	id: string
	username: string
	content: string
	article: {
		id: string
		title: string
	},
	commentStatus: number
	email: string
	IP: string
	region: string
	fatherComment: string
	createTime: string
}

onMounted(() => {
	getComment()
})

let ids:any=[];
function handleSelectionChange(val:[any]) {
	val.forEach(value => {
		ids.push(value.id)
	})
}
let tableDate = reactive<Array<ITableData | any>>([])
let page = 1;
let hasMore = true

/**
 * 加载评论
 */
async function getComment() {
	const response = await get(`/comment/list?page=${page}&limit=20`);
	if (response.code == 200) {
		const data: Array<ITableData> = response.data.list;
		if (data.length == 0) {
			hasMore = false
		}
		if (page == 1) {
			tableDate.length = 0
		}
		data.forEach((value, index) => {
			tableDate.push(value)
		})
	}
}

//监听滚动加载更多数据
watch(() => store.state.scrollCount, () => {
	if (hasMore) {
		page++;
		getComment();
	}
})
let showReplayCard = ref(false)

//关闭弹窗
function doClose() {
	showReplayCard.value = false;
}

//通过审核
async function doExamine(id: string) {
	const response = await get(`/comment/confirmExaminations?ids=${id}`);
	if (response.code == 200) {
		success("成功！")
	}
}

//批量通过审核
async function batchConfirm() {
	let idsString:string='';
	ids.forEach((value: string)=>{
		idsString+=value+","
	})
	const response = await get(`/comment/confirmExaminations?ids=${idsString.slice(0,-1)}`);
	if (response.code == 200) {
		success("成功！")
	}
}

let replay = reactive({
	articleId: '',
	fatherCommentId: '',
	replayName: '',
	content: ''
})

//点击回复
function doAnswer(id: string, articleId: string, username: string) {
	showReplayCard.value = true;
	replay.replayName = username
	replay.fatherCommentId = id;
	replay.articleId = articleId;
	console.log(replay)
}

//回复某人
async function doReplay() {
	const response = await post('/comment/replay', replay);
	if (response.code == 200) {
		page = 1;
		await getComment();
		success("回复成功!")
		doClose()
	}
}

//点击删除
async function doDel(id: string) {
	const response = await get(`/comment/delete?ids=${id}`);
	if (response.code == 200) {
		success("删除成功!")
	}
}

async function batchDel() {
	console.log(ids)
}

function success(msg: string) {
	page = 1;
	getComment().then(() => {
		const scrollElement: any = document.getElementById("content");
		scrollElement.scrollTo({
			top: 0,
			behavior: "smooth"
		});
		proxy.$notify({
			title: '成功',
			message: msg,
			type: 'success'
		})
	});
}


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
.btn-area{
	width: 100%;
	display: flex;
	justify-content: flex-end;
	margin: 0 20px 10px 0
}

.operator {
	display: flex;
	align-items: center;
	justify-content: space-around;

	.examine{
		width: 19px;
		height: 19px;
	}

	.answer {
		width: 18px;
		height: 18px;
	}

	img {
		width: 20px;
		height: 20px;
		cursor: pointer;
	}
}

.content {
	border-radius: 20px;
	background-color: white;
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	align-items: center;
	width: 500px;
	height: 350px;

	.toWho {
		width: 90%;
		height: 60px;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 10px;

		span {
			font-size: 18px;
			font-weight: normal;
			color: #4197ef;
			user-select: none;
		}

		#object {
			width: 20px;
			height: 20px;
			color: #00ccff;
		}

		img {
			width: 20px;
			height: 20px;
			color: #00ccff;
			cursor: pointer;
			transition: all .3s ease;
			border-radius: 50%;

			&:hover {
				background-color: #f56c6c;
				border-radius: 50%;
				transform: rotateZ(360deg);
			}
		}
	}

	textarea {
		width: 90%;
		height: 250px;
		border: 1px solid #dedede;
		outline: none;
		transition: all 0.3s ease;
		border-radius: 20px;
		color: #2a2a2a;
		font-size: 15px;
		line-height: 25px;
		Padding: 10px;

		&::-webkit-scrollbar {
			width: 0;
		}

		resize: none;

		&::-webkit-input-placeholder {
			color: #a1a1a1;
			font-size: inherit;
		}
	}

	textarea:focus {
		border: 2px solid rgb(78, 113, 242);
	}

	.button {
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: flex-end;
		height: 60px;

		.el-button.el-button--primary.is-round {
			width: 100px;
			margin-right: 20px;
		}
	}
}


</style>
