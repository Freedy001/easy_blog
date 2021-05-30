<!--suppress CssInvalidPseudoSelector, JSUnresolvedFunction, JSUnresolvedVariable -->
<template>
	<div class="indexContainer">
		<el-tabs v-model="activeName">
			<el-tab-pane label="常规设置" v-if="commonReg.test(store.state.userInfo.permissionStr)" name="1">
				<div class="common-setting">
					<div class="item">
						<h1>博客标题</h1>
						<el-input v-model="common.blogTitle" placeholder="请输入内容"></el-input>
					</div>
					<div class="item">
						<h1>博客域名</h1>
						<el-input v-model="common.webSiteDomainName" placeholder="请输入内容"></el-input>
					</div>
					<div class="item">
						<h1>首页文章</h1>
						<el-select
								v-model="common.indexArticle.id"
								filterable
								remote
								reserve-keyword
								placeholder="请输入关键词"
								:remote-method="querySearch"
								:loading="loading">
							<el-option
									v-for="item in recommend"
									:key="item.id"
									:label="item.label"
									:value="item.id">
							</el-option>
						</el-select>
					</div>
					<div class="logo-img item">
						<div class="logo">
							<h1>Logo</h1>
							<img :src="loadResource(common.logo)"
							     @click="changeLogo"
							     alt="">
						</div>
						<div class="img">
							<h1>首页图片</h1>
							<img :src="loadResource(common.poster)"
							     @click="changePoster"
							     alt="">
						</div>
					</div>
					<div class="item color">
						<h1>首页色系</h1>
						<el-color-picker v-model="common.indexColor" show-alpha class="color-picker"></el-color-picker>
						<div class="show-box">
							<div class="triangle" :style="triangle"></div>
							<img :src="loadResource(common.poster)" alt="">
						</div>
					</div>
					<div class="item">
						<h1>页脚信息</h1>
						<el-input
								type="textarea"
								:rows="2"
								placeholder="可以填入HTML"
								v-model="common.footInfo">
						</el-input>
					</div>
					<el-button type="primary" @click="saveCommon">保存</el-button>
				</div>
			</el-tab-pane>
			<el-tab-pane label="SMTP设置" v-if="smtpReg.test(store.state.userInfo.permissionStr)" name="2">
				<div class="smtp">
					<div class="item">
						<h1>SMTP地址</h1>
						<el-input v-model="SMPTData.emailHostName" placeholder="请输入SMTP地址"></el-input>
					</div>
					<div class="item">
						<h1>SSL端口</h1>
						<el-input v-model="SMPTData.sslPort" placeholder="请输入SSL端口"></el-input>
					</div>
					<div class="item">
						<h1>邮箱账号</h1>
						<el-input v-model="SMPTData.emailFrom" placeholder="请输入邮箱账号"></el-input>
					</div>
					<div class="item">
						<h1>邮箱密码或授权码</h1>
						<el-input v-model="SMPTData.emailAuthentication" placeholder="请输入邮箱密码或授权码"></el-input>
					</div>
					<div class="item">
						<h1>发送人</h1>
						<el-input v-model="SMPTData.senderName" placeholder="请输入发送人"></el-input>
					</div>
					<el-button type="primary" @click="saveSMTP">保存</el-button>
				</div>
			</el-tab-pane>
			<el-tab-pane label="评论设置" v-if="commentReg.test(store.state.userInfo.permissionStr)" name="3">
				<div class="comment">
					<div class="item">
						<h1>评论需要审核</h1>
						<el-switch v-model="comment.examination">
						</el-switch>
					</div>
					<div class="item">
						<h1>新评论通知</h1>
						<el-switch v-model="comment.newCommentNotification">
						</el-switch>
					</div>
					<div class="item">
						<h1>评论回复通知对方</h1>
						<el-switch v-model="comment.replayNotification">
						</el-switch>
					</div>
					<el-button type="primary" @click="savaComment"
					           style="margin-top: 30px">保存
					</el-button>
				</div>
			</el-tab-pane>
			<el-tab-pane label="关于页面" v-if="aboutReg.test(store.state.userInfo.permissionStr)" name="4">
				<div class="other">
					<div class="item">
						<div class="about">
							<h1>关于</h1>
							<el-button type="primary" @click="$router.push('/index/article?id=1')" round>去编辑</el-button>
						</div>
						<div class="root">
							<article class="markdown-body" id="markdown" v-html="article.content">
							</article>
						</div>
					</div>
				</div>
			</el-tab-pane>
		</el-tabs>
		<ImgDrawer :isDrawer="showDrawer"
		           @clickCallback="clickCallback">
		</ImgDrawer>
	</div>
</template>

<script setup lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {get, getFrontApi, loadResource, post} from "../http";
import ImgDrawer from '../components/ImgDrawer.vue'
import {ElMessage} from "element-plus";
import {copyProperties, noPermission} from "../util/Common";
import {useRoute} from "vue-router";
import {useStore} from "vuex";

const {proxy}: any = getCurrentInstance();
defineComponent({
	ImgDrawer
})
const route = useRoute();
const store = useStore();
let commonReg = /setting-common/
let smtpReg = /setting-smtp/
let commentReg = /setting-comment/
let aboutReg = /setting-about/

onMounted(() => {
	if (commonReg.test(store.state.userInfo.permissionStr)) {
		initCommonValue();
	}
	// initSMTPValue();
	// initCommentValue();
	// initAbout();
	if (route.query.toForth) {
		activeName.value = "4"
	}
})

let showDrawer = ref(0)
let activeName = ref("1")
watch(activeName, (tab) => {
	if (tab == "1") {
		initCommonValue();
	} else if (tab == "2") {
		initSMTPValue();
	} else if (tab == "3") {
		initCommentValue();
	} else if (tab == "4") {
		initAbout();
	}
})

//***************************常规设置***********************************
interface ICommon {
	blogTitle: string,
	indexArticle: {
		id: string
		title: string
	},
	logo: string,
	poster: string,
	indexColor: string,
	footInfo: string
	webSiteDomainName:string
}

//页面的数据
let common = reactive<any>({
	indexArticle: {
		id: '',
		title: ''
	}
});
//透明三角形样式
let triangle = reactive({
	"borderBottom": "500vh solid rgba(45, 37, 73, 0.7)"
})
watch(() => common.indexColor, () => {
	triangle["borderBottom"] = `500vh solid ${common.indexColor}`
})

let loading = ref(false)
//建议数据
let recommend = reactive<any>([])

watch(() => common.indexArticle.id, (val) => {
	recommend.forEach((item: { id: any, label: any }) => {
		if (item.id == val) {
			common.indexArticle.title = item.label
		}
	})
})


//获取文章建议
async function querySearch(queryString: string) {
	loading.value = true
	//这里要掉前台的接口
	const response = await getFrontApi(`/frontend/search/getSuggestions?queryString=${queryString}`);
	if (response.code == 200) {
		recommend.length = 1;
		const data: Array<any> = response.data;
		data.forEach((value, index) => {
			if (recommend[0].id != value.id) {
				recommend.push({
					id: value.id,
					label: value.title,
				})
			}
		})
		loading.value = false;
	} else if (response.code === 3001) {
		noPermission()
	}
}

//表示要更换图片的那个一项
let chose: string;

//点击logo
function changeLogo() {
	showDrawer.value++;//打开抽屉
	chose = "logo";
}

//点击首页图片
function changePoster() {
	showDrawer.value++;//打开抽屉
	chose = "poster";
}

//选择图片后的回调
function clickCallback(url: string) {
	common[chose] = url;
}

//初始化常规设置 (数据回显)
async function initCommonValue() {
	const response = await get('/setting/getCommonSetting');
	if (response.code == 200) {
		const data: any | ICommon = response.data;
		copyProperties(data, common)
		recommend.push({
			id: data.indexArticle.id,
			label: data.indexArticle.title,
		})
	} else if (response.code == 3001) {
		noPermission()
	} else {
		proxy.$notify({
			title: '出差啦😢！',
			message: response.msg,
			type: 'error'
		});
	}
}

//保存数据
async function saveCommon() {
	const response = await post('/setting/saveCommon', common);
	if (response.code == 200) {
		proxy.$notify({
			title: '成功',
			message: '保存成功!',
			type: 'success'
		});
	} else if (response.code == 3001) {
		noPermission()
	} else {
		proxy.$notify({
			title: '出差啦😢！',
			message: response.msg,
			type: 'error'
		});
	}
}

//*****************************SMTP设置**********************************
interface ISMPTData {
	emailHostName: string
	sslPort: string
	emailFrom: string
	emailAuthentication: string
	senderName: string
}

let SMPTData = reactive<ISMPTData | any>({})

//初始化smtp数据
async function initSMTPValue() {
	const response = await get('/setting/getSMTPSetting');
	if (response.code == 200) {
		const data: any = response.data;
		Object.keys(data).forEach((value, index) => {
			SMPTData[value] = data[value];
		})
	} else if (response.code == 3001) {
		noPermission()
	} else {
		proxy.$notify({
			title: '出差啦😢！',
			message: response.msg,
			type: 'error'
		});
	}
}

//保存smtp设置
async function saveSMTP() {
	const response = await post('/setting/saveSMTP', SMPTData);
	if (response.code == 200) {
		proxy.$notify({
			title: '成功',
			message: '保存成功!',
			type: 'success'
		});
	} else if (response.code == 3001) {
		noPermission()
	} else {
		proxy.$notify({
			title: '出差啦😢！',
			message: response.msg,
			type: 'error'
		});
	}
}

//*******************************评论设置*****************************************
let comment = reactive<any>({});

async function initCommentValue() {
	const response = await get('/setting/getCommentSetting');
	if (response.code == 200) {
		const data: any = response.data;
		Object.keys(data).forEach((value, index) => {
			comment[value] = data[value];
		})
	} else if (response.code == 3001) {
		noPermission()
	} else {
		proxy.$notify({
			title: '出差啦😢！',
			message: response.msg,
			type: 'error'
		});
	}
}

//保存评论数据
async function savaComment() {
	const response = await post('/setting/saveComment', comment);
	if (response.code == 200) {
		proxy.$notify({
			title: '成功',
			message: '保存成功!',
			type: 'success'
		});
	} else if (response.code == 3001) {
		noPermission()
	} else {
		proxy.$notify({
			title: '出差啦😢！',
			message: response.msg,
			type: 'error'
		});
	}
}

//********************************关于页面**************************************
let article = reactive<any>({})

async function initAbout() {
	const response = await getFrontApi('/frontend/article/get?id=1')
	if (response.code == 200) {
		copyProperties(response.data, article)
	} else if (response.code == 3001) {
		noPermission()
	}
}

</script>

<style scoped lang="scss">
.indexContainer {
	.common-setting {

		.el-select {
			width: 100%;
			margin-bottom: 10px;
		}

		.logo-img {
			display: flex;
			justify-content: space-around;

			img {
				cursor: pointer;
				border-radius: 20px;
				width: 280px;
				height: 280px;
			}
		}

		.color {
			position: relative;

			:deep(.color-picker) {
				width: 100%;
				height: 0;
				z-index: 99;
				opacity: 0;
			}

			:deep(.el-color-picker__trigger) {
				border: none;
				width: 800px;
				height: 500px;
				position: absolute;
				top: 0;
				left: 50%;
				transform: translateX(-50%);
			}

			.show-box {
				box-sizing: content-box;
				overflow: hidden;
				position: relative;
				border-radius: 20px;
				width: 800px;
				height: 500px;
				margin: 0 auto;

				.triangle {
					position: absolute;
					top: 0;
					left: 0;
					border-right: 200vw solid rgba(36, 145, 191, 0);
					transform: translate(0, -30vh);
					transition: all 0.3s;
				}

				img {
					width: 110%;
					height: 110%;
					object-fit: cover;
				}
			}
		}

		:deep(.el-textarea__inner) {
			height: 300px;
		}
	}

	.smtp {

	}

	.comment {

	}

	.item {
		margin: 30px 0;

		h1 {
			font-size: 20px;
			font-weight: normal;
			color: #535353;
			margin-bottom: 10px;
		}
	}

	.about {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.markdown-body {
		box-sizing: border-box;
		min-width: 200px;
		max-width: 760px;
		margin: 0 auto;
		padding: 50px 0 100px 0;
	}
}
</style>