<!--suppress JSUnresolvedVariable -->
<template>
	<div class="root">
		<div class="index-container">
			<el-card class="box-card left">
				<div class="info">
					<div class="photo" @click="changeHeadImg" style="cursor: pointer">
						<img :src="loadResource(userInfoDetail.headImg)" alt="">
					</div>
					<p v-if="userInfoDetail.rootAdmin" class="name">Admin</p>
					<p class="name">{{ userInfoDetail.nickname }}</p>
				</div>
				<div class="introduce">
					<div class="item">
						<i class="el-icon-ice-cream-round"></i>
						<a href="#" style="color: #0b9aff;text-decoration: none">
							http://localhost:3000{{ userInfoDetail.pageUrl }}
						</a>
					</div>
					<div class="item">
						<i class="el-icon-ice-cream-square"></i>
						<p>{{ userInfoDetail.email }}</p>
					</div>
					<div class="item">
						<i class="el-icon-lollipop"></i>
						<p>{{ userInfoDetail.createDuration }}</p>
					</div>
				</div>
				<el-divider><i class="el-icon-edit"></i></el-divider>
				<div class="statistics">
					<p>累计发表了 {{ userInfoDetail.totalArticle }} 篇文章。</p>
					<el-divider></el-divider>
					<p>累计创建了 {{ userInfoDetail.totalCategory }} 个分类。</p>
					<el-divider></el-divider>
					<p>累计创建了 {{ userInfoDetail.totalTags }} 个标签。</p>
					<el-divider></el-divider>
					<p>累计获得了 {{ userInfoDetail.totalComment }} 条评论。</p>
					<el-divider></el-divider>
					<p>文章总阅读 {{ userInfoDetail.totalVisit }} 次。</p>
					<el-divider></el-divider>

				</div>
			</el-card>
			<el-card class="box-card right">
				<h1>个人资料</h1>
				<el-tabs v-model="activeName" @tab-click="handleClick">
					<el-tab-pane label="基本资料" name="first">
						<el-form ref="form"
						         :model="userInfoDetail"
						         :rules="userInfoRules">
							<el-form-item label="登录用户名" prop="username" style="margin-bottom: 15px">
								<el-input v-model="userInfoDetail.username"></el-input>
							</el-form-item>
							<el-form-item label="昵称" prop="nickname" style="margin-bottom: 15px">
								<el-input v-model="userInfoDetail.nickname"></el-input>
							</el-form-item>
							<el-form-item label="电子邮件" prop="email" style="margin-bottom: 15px">
								<el-input v-model="userInfoDetail.email"></el-input>
							</el-form-item>
							<el-form-item label="个人说明" style="margin-bottom: 15px">
								<el-input type="textarea" placeholder="可以是普通文本或者html" v-model="userInfoDetail.introduce"></el-input>
							</el-form-item>
							<el-form-item>
								<el-button type="primary" @click="saveInfo">保存</el-button>
							</el-form-item>
						</el-form>
					</el-tab-pane>
					<el-tab-pane label="修改密码" name="second">
						<el-form :model="ruleForm"
						         status-icon
						         :rules="rules"
						         class="demo-ruleForm">
							<el-form-item label="原密码" prop="oldPass">
								<el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
							</el-form-item>
							<el-form-item label="新密码" prop="pass">
								<el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
							</el-form-item>
							<el-form-item label="确认密码" prop="checkPass">
								<el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
							</el-form-item>
							<el-form-item>
								<el-button type="primary" @click="changePassword">提交</el-button>
								<el-button @click="resetForm">重置</el-button>
							</el-form-item>
						</el-form>
					</el-tab-pane>
					<el-tab-pane label="用户管理" name="third">
						<div style="text-align: right;cursor: pointer" @click="openNewUserWindow">
							<i class="el-icon-circle-plus-outline" style="margin-right: 5px"></i>
							<span>添加一个</span>
						</div>
						<el-table
								:data="userManagement"
								style="width: 100%">
							<el-table-column type="expand">
								<template #default="props">
									<el-card>
										<div class="expand">
											<div class="info">
												<div class="photo">
													<img :src="loadResource(props.row.headImg)" alt="">
												</div>
												<p class="name">{{ props.row.nickname }}</p>
											</div>
											<div class="introduce">
												<div class="item email">
													<div>
														<i class="el-icon-ice-cream-square"></i>
														<p>{{ props.row.createDuration }}</p>
													</div>
												</div>
												<div class="item email">
													<div>
														<i class="el-icon-ice-cream-square"></i>
														<p>{{ props.row.email }}</p>
													</div>
												</div>
												<div class="item text">
													<span>个人介绍</span>
													<p>{{ props.row.introduce }}</p>
												</div>
											</div>
										</div>
									</el-card>
								</template>
							</el-table-column>
							<el-table-column
									width="150px"
									prop="username"
									label="用户名">
							</el-table-column>
							<el-table-column
									prop="status"
									label="状态">
							</el-table-column>
							<el-table-column
									label="操作">
								<template #default="prop">
									<el-tooltip content="设置" placement="top">
										<i class="el-icon-setting" style="font-size: 20px;margin-right: 15px;cursor: pointer;"
										   @click="userSetting(prop.row.id)"></i>
									</el-tooltip>
									<el-tooltip content="删除" placement="top">
										<i class="el-icon-milk-tea" style="font-size: 20px;cursor: pointer;"
										   @click="userDelete(prop.row.id)"></i>
									</el-tooltip>
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
				</el-tabs>
			</el-card>
		</div>
		<transition name="el-fade-in-linear">
			<FullScreen v-if="showCard" @click="showCard=false" :opacity="0.5">
				<el-card class="permission-card" @click.stop="tip=''">
					<div class="newUserForm">
						<div class="item">
							<span>用户名:</span>
							<el-input placeholder="请输入用户名" v-model="newUser.username"></el-input>
						</div>
						<div class="item">
							<span>密码:</span>
							<el-input placeholder="请输入密码" show-password v-model="newUser.password"></el-input>
						</div>
						<div class="item">
							<span>邮箱:</span>
							<el-input placeholder="请输入邮箱" v-model="newUser.email"></el-input>
						</div>
						<el-divider class="outer-divider"></el-divider>
						<div class="permission item" v-if="PermissionItem.isManager!==null&&PermissionItem.isManager">
							<div style="width: 100%"><p style="margin-left: 10px">权限:</p></div>
							<div class="permissionContainer">
								<span>文章:</span>
								<div class="article line">
									<div style="display: flex;justify-content: space-between">
										<el-checkbox style="margin-bottom: 10px"
										             :indeterminate="selectAll.articleIsIndeterminate"
										             v-model="selectAll.articleCheckAll"
										             @change="handleArticleCheckAllChange">全选
										</el-checkbox>
									</div>
									<el-checkbox-group
											v-model="newUser.articlePermission"
											@change="handleArticleCheckedCitiesChange">
										<el-checkbox v-for="(item,i) in PermissionItem.articlePermission" :key="i"
										             :label="item"></el-checkbox>
									</el-checkbox-group>
								</div>
								<el-divider></el-divider>
								<span>评论:</span>
								<div class="user line">
									<div style="display: flex;justify-content: space-between">
										<el-checkbox style="margin-bottom: 10px"
										             :indeterminate="selectAll.commentIsIndeterminate"
										             v-model="selectAll.commentCheckAll"
										             @change="handleCommentCheckAllChange">全选
										</el-checkbox>
									</div>
									<el-checkbox-group
											v-model="newUser.commentPermission"
											@change="handleCommentCheckedCitiesChange">
										<el-checkbox v-for="(item,i) in PermissionItem.commentPermission" :key="i"
										             :label="item"></el-checkbox>
									</el-checkbox-group>
								</div>
								<el-divider></el-divider>
								<span>用户:</span>
								<div class="user line">
									<div style="display: flex;justify-content: space-between">
										<el-checkbox style="margin-bottom: 10px"
										             :indeterminate="selectAll.userIsIndeterminate"
										             v-model="selectAll.userCheckAll"
										             @change="handleUserCheckAllChange">全选
										</el-checkbox>
									</div>
									<el-checkbox-group
											v-model="newUser.userPermission"
											@change="handleUserCheckedCitiesChange">
										<el-checkbox v-for="(item,i) in PermissionItem.userPermission" :key="i"
										             :label="item"></el-checkbox>
									</el-checkbox-group>
								</div>
								<el-divider></el-divider>
								<span>设置:</span>
								<div class="setting line">
									<div style="display: flex;justify-content: space-between">
										<el-checkbox style="margin-bottom: 10px"
										             :indeterminate="selectAll.settingIsIndeterminate"
										             v-model="selectAll.settingCheckAll"
										             @change="handleSettingCheckAllChange">全选
										</el-checkbox>
									</div>
									<el-checkbox-group
											v-model="newUser.settingPermission"
											@change="handleSettingCheckedCitiesChange">
										<el-checkbox v-for="(item,i) in PermissionItem.settingPermission" :key="i"
										             :label="item"></el-checkbox>
									</el-checkbox-group>
								</div>
							</div>
						</div>
						<el-divider class="outer-divider" v-if="PermissionItem.isManager!==null&&PermissionItem.isManager"></el-divider>
						<div class="item">
							<el-button type="primary" :class="{'shake-horizontal':tip!==''}" @click.stop="createOrUpdateNewUser">保存
							</el-button>
							<el-button @click="showCard=false">取消</el-button>
							<span style="color: #ff268b;margin-left: 20px;width: 100%">{{ tip }}</span>
						</div>
					</div>
				</el-card>
			</FullScreen>
		</transition>
		<ImgDrawer
				:isDrawer="drawer"
				@clickCallback="handlePickPic"
		></ImgDrawer>
	</div>
</template>

<script setup lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {get, loadResource, post} from "../http";
import {useRouter} from "vue-router";
import ImgDrawer from '../components/ImgDrawer.vue'
import FullScreen from '../components/FullScreen.vue'
import {useStore} from "vuex";
import {copyProperties, copyPropertiesHasNull} from "../util/Common";

const store = useStore();
const router = useRouter();
const {proxy}: any = getCurrentInstance();
defineComponent({
	ImgDrawer,
	FullScreen
})
let drawer = ref(false)

//更换头像
function changeHeadImg() {
	drawer.value = !drawer.value;
}

//更换头像
async function handlePickPic(url: string) {
	const response = await post('/manager/updateUserInfo', {
		headImg: url,
		username: userInfoDetail.username
	});
	if (response.code == 200) {
		initData().then()
		store.commit('notifyReloadNickNameAndHeadImg')
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

//**********************************校验**********************************start
let userInfoRules = reactive({
	username: [
		{required: true, message: '用户名不能为空', trigger: 'blur'},
		{min: 5, max: 20, message: '用户名长度在 5 到 20 个字符', trigger: 'blur'}
	],
	nickname: [
		{required: true, message: '昵称不能为空', trigger: 'blur'},
		{min: 5, max: 20, message: '密码长度在 8 到 20 个字符！', trigger: 'blur'}
	],
	email: [
		{required: true, message: '请输入邮箱地址', trigger: 'blur'},
		{type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
	]
})


//密码验证表单
let ruleForm = reactive({
	oldPass: '',
	pass: '',
	checkPass: ''
})
//密码校验
let rules = reactive({
	oldPass: [
		{validator: validatePass, trigger: 'blur'}
	],
	pass: [
		{validator: validatePass, trigger: 'blur'}
	],
	checkPass: [
		{validator: validatePass2, trigger: 'blur'}
	]
})

function validatePass(rule: any, value: string, callback: any) {
	if (value != '') {
		callback()
	} else {
		callback(new Error("请输入密码！"))
	}
}

function validatePass2(rule: any, value: any, callback: any) {
	if (ruleForm.pass == ruleForm.checkPass) {
		callback();
	} else {
		callback(new Error('两次输入密码不一致!'));
	}
}

//重置密码表单
function resetForm() {
	ruleForm.pass = '';
	ruleForm.checkPass = '';
}

//**********************************校验**********************************end
//******************************tab1下面方法与参数**********************************start
interface userInfo {
	username: string,
	nickname: string,
	email: string,
	introduce: string,
	headImg: string | null,
	rootAdmin: boolean,
	pageUrl: string,
	createDuration: string
	totalArticle: number,
	totalCategory: number,
	totalTags: number,
	totalComment: number
	totalVisit: number
}

let userInfoDetail: userInfo = reactive<userInfo>({
	username: '',
	nickname: '',
	email: '',
	introduce: '',
	headImg: null,
	rootAdmin: false,
	pageUrl: '',
	createDuration: '',
	totalArticle: 0,
	totalCategory: 0,
	totalTags: 0,
	totalComment: 0,
	totalVisit: 0,
})
//数据回显
onMounted(async () => {
	initData().then();
})
let page = 1

/**
 * 获取个人用户消息
 */
async function initData() {
	const response = await get('/manager/getUserInfo');
	if (response.code == 200) {
		const resData: userInfo = response.data
		copyProperties(resData, userInfoDetail)
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

async function saveInfo() {
	const response = await post('/manager/updateUserInfo', {
		username: userInfoDetail.username,
		nickname: userInfoDetail.nickname,
		email: userInfoDetail.email,
		introduce: userInfoDetail.introduce,
	});
	if (response.code == 2002) {
		proxy.$notify({
			title: '成功',
			message: '修改成功，请重新登录!',
			type: 'success'
		})
		await router.push('/login');
	} else if (response.code == 200) {
		store.commit('notifyReloadNickNameAndHeadImg')
		proxy.$notify({
			title: '成功',
			message: '修改成功!',
			type: 'success'
		})
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

async function changePassword() {
	const response = await post('/manager/updateUserPassword', {
		oldPassword: ruleForm.oldPass,
		newPassword: ruleForm.checkPass
	})
	if (response.code == 2002) {
		proxy.$notify({
			title: '成功',
			message: '修改成功，请重新登录!',
			type: 'success'
		})
		await router.push('/login');
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

//******************************tab3下面方法与参数**********************************start
interface IUserManagement {
	id: number
	username: string
	nickname: string
	headImg: string
	email: string
	createDuration: string
	introduce: string
	status: string
}

let userManagement = reactive<Array<IUserManagement>>([])

let activeName = ref('first')

function handleClick() {
	if (activeName.value == 'third') {
		userManagement.length = 0
		loadUserList()
	}
}

//加载用户列表
async function loadUserList() {
	const response = await get(`/manager/list??page=${page}&limit=20`);
	if (response.code == 200) {
		const data: Array<IUserManagement> = response.page.list
		data.forEach((value, index) => {
			userManagement.push({
				id: value.id,
				username: value.username,
				nickname: value.nickname,
				headImg: value.headImg,
				email: value.email,
				createDuration: value.createDuration,
				introduce: value.introduce,
				status: value.status,
			})
		})
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

//下面是添加用户
let showCard = ref(false)

interface IPermissionItem {
	articlePermission: Array<string>
	commentPermission: Array<string>
	userPermission: Array<string>
	settingPermission: Array<string>
}

//所有权限的列表
let PermissionItem: IPermissionItem = reactive<IPermissionItem>({
	articlePermission: [],
	commentPermission: [],
	userPermission: [],
	settingPermission: []
});

//打开创建新用户窗口
async function openNewUserWindow() {
	emptyNewUser()
	const response = await get('/rolePermission/getPermissionItem');
	if (response.code == 200) {
		showCard.value = true;
		const item: IPermissionItem = response.data
		copyProperties(item,PermissionItem)
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

function emptyNewUser() {
	if (newUser.id != null) {
		newUser.id = null
		newUser.username = ''
		newUser.password = ''
		newUser.email = ''
		newUser.articlePermission = []
		newUser.commentPermission = []
		newUser.settingPermission = []
		newUser.userPermission = []
		Object.keys(selectAll).forEach(key => selectAll[key] = false)
	}
}

interface INewUser extends IPermissionItem {
	id: number | any
	username: string | any
	password: string | any
	email: string | any
}

//创建用户的表单
let newUser = reactive<INewUser>({
	id: null,
	username: null,
	password: null,
	email: null,
	articlePermission: [],
	commentPermission: [],
	userPermission: [],
	settingPermission: [],
})
//******************************下面是全选功能******************************start
let selectAll = reactive<any>({
	articleCheckAll: false,
	commentCheckAll: false,
	userCheckAll: false,
	settingCheckAll: false,
	articleIsIndeterminate: false,
	commentIsIndeterminate: false,
	userIsIndeterminate: false,
	settingIsIndeterminate: false,
})

function handleArticleCheckAllChange(val: any) {
	newUser.articlePermission = val ? PermissionItem.articlePermission : []
	selectAll.articleIsIndeterminate = false;
}

function handleCommentCheckAllChange(val: any) {
	newUser.commentPermission = val ? PermissionItem.commentPermission : []
	selectAll.commentIsIndeterminate = false;
}

function handleUserCheckAllChange(val: any) {
	newUser.userPermission = val ? PermissionItem.userPermission : []
	selectAll.userIsIndeterminate = false;
}

function handleSettingCheckAllChange(val: any) {
	newUser.settingPermission = val ? PermissionItem.settingPermission : []
	selectAll.settingIsIndeterminate = false;
}

function handleArticleCheckedCitiesChange(value: any) {
	let checkedCount = value.length;
	selectAll.articleCheckAll = checkedCount === PermissionItem.articlePermission.length;
	selectAll.articleIsIndeterminate = checkedCount > 0 && checkedCount < PermissionItem.articlePermission.length
}

function handleCommentCheckedCitiesChange(value: any) {
	let checkedCount = value.length;
	selectAll.commentCheckAll = checkedCount === PermissionItem.commentPermission.length;
	selectAll.commentIsIndeterminate = checkedCount > 0 && checkedCount < PermissionItem.commentPermission.length
}

function handleUserCheckedCitiesChange(value: any) {
	let checkedCount = value.length;
	selectAll.userCheckAll = checkedCount === PermissionItem.userPermission.length;
	selectAll.userIsIndeterminate = checkedCount > 0 && checkedCount < PermissionItem.userPermission.length
}

function handleSettingCheckedCitiesChange(value: any) {
	let checkedCount = value.length;
	selectAll.settingCheckAll = checkedCount === PermissionItem.settingPermission.length;
	selectAll.settingIsIndeterminate = checkedCount > 0 && checkedCount < PermissionItem.settingPermission.length
}

//******************************上面是全选功能******************************end
let tip = ref('')

/**
 * 创建或更改用户
 */
async function createOrUpdateNewUser() {
	try {
		valid();
		const response = await post('/manager/createOrUpdateManager', newUser)
		if (response.code == 200) {
			proxy.$notify({
				title: '成功',
				message: '操作成功！',
				type: 'success'
			})
			userManagement.length = 0
			loadUserList().then();
			showCard.value = false;
			newUser.id = null;
			newUser.username = null;
			newUser.password = null;
			newUser.email = null;
			newUser.articlePermission = [];
			newUser.commentPermission = [];
			newUser.userPermission = [];
			newUser.settingPermission = [];
		} else if (response.code == 2002) {
			proxy.$notify({
				title: '成功',
				message: '修改成功，请重新登录!',
				type: 'success'
			})
			await router.push('/login');
		} else {
			proxy.$notify.error({
				title: '出差啦😢！',
				message: response.msg,
				duration: 5000
			})
		}
	} catch (e) {
		//验证失败
	}
}

function valid() {
	if (newUser.id!=null){
		if (newUser.password!=null&&(newUser.password.length < 8 || newUser.password.length > 20)) {
			tip.value = '密码长度在 8 到 20 个字符！'
			throw new Error();
		}else if (!newUser.email.match('^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$')) {
			tip.value = '邮箱格式不正确~~ 😥😥'
			throw new Error();
		}
	} else if (!hasText(newUser.username) || !hasText(newUser.email) || !hasText(newUser.password)) {
		tip.value = '用户名密码邮箱都不能为空哦！'
		throw new Error();
	} else if (newUser.username.length < 5 || newUser.username.length > 20) {
		tip.value = '用户名长度在 5 到 20 个字符！'
		throw new Error();
	} else if (newUser.password.length < 8 || newUser.password.length > 20) {
		tip.value = '密码长度在 8 到 20 个字符！'
		throw new Error();
	} else if (!newUser.email.match('^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$')) {
		tip.value = '邮箱格式不正确~~ 😥😥'
		throw new Error();
	}
}

function hasText(str: string) {
	return str != null && str.length > 0
}

/**
 * 用户的相关设置 回显数据
 */
async function userSetting(id: number) {
	await openNewUserWindow()
	const response = await get(`/manager/getUserImportantInfo?id=${id}`)
	if (response.code == 200) {
		const data: INewUser = response.data
		copyPropertiesHasNull(data, newUser)
		Object.keys(selectAll).forEach(key => selectAll[key] = false)
		if (newUser.userPermission.length > 0) {
			selectAll.userIsIndeterminate = true
		}
		if (newUser.settingPermission.length > 0) {
			selectAll.settingIsIndeterminate = true
		}
		if (newUser.commentPermission.length > 0) {
			selectAll.commentIsIndeterminate = true
		}
		if (newUser.articlePermission.length > 0) {
			selectAll.articleIsIndeterminate = true
		}
		if (newUser.articlePermission.length === PermissionItem.articlePermission.length) {
			selectAll.articleIsIndeterminate = false
			selectAll.articleCheckAll = true
		}
		if (newUser.commentPermission.length === PermissionItem.commentPermission.length) {
			selectAll.commentIsIndeterminate = false
			selectAll.commentCheckAll = true
		}
		if (newUser.userPermission.length === PermissionItem.userPermission.length) {
			selectAll.userIsIndeterminate = false
			selectAll.userCheckAll = true
		}
		if (newUser.settingPermission.length === PermissionItem.settingPermission.length) {
			selectAll.settingIsIndeterminate = false
			selectAll.settingCheckAll = true
		}
		//打开用户消息窗口
		showCard.value = true;
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

/**
 * 删除用户
 */
async function userDelete(id: number) {
	const response = await get(`/manager/delete?ids=${id}`)
	if (response.code == 200) {
		proxy.$notify({
			title: '成功',
			message: '删除成功！',
			type: 'success'
		})
		userManagement.length = 0
		loadUserList().then();
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg
		})
	}
}

//******************************tab3下面方法与参数**********************************end
</script>

<style scoped lang="scss">
.root {
	height: 100%;

	.index-container {
		display: flex;
		width: 100%;
		justify-content: space-between;

		.box-card.left {
			width: 49%;
			height: 660px;
			display: flex;
			flex-direction: column;
			justify-content: center;

			.photo {
				width: 70px;
				height: 70px;
				box-sizing: content-box;
				margin: auto;
				border-radius: 50%;
				background: #fff;
				overflow: hidden;
				transition: all .3s;

				img {
					width: 100%;
					height: 100%;
					object-fit: cover;
				}
			}

			.name {
				margin-top: 15px;
				color: #000000;
				text-align: center;
				font-size: 25px;
			}

			.introduce {
				margin: 20px;

				.item {
					margin: 10px;

					p {
						display: inline;
					}
				}
			}

			.statistics {
				padding-top: 20px;
				margin: 20px;

				.el-divider {
					margin-top: 15px;
					margin-bottom: 15px;
				}
			}
		}

		.box-card.right {
			width: 49%;
			height: 80%;

			h1 {
				font-size: 20px;
				font-weight: lighter;
			}

			::v-deep(#pane-first .el-form-item) {
				margin-bottom: 0;
			}

			::v-deep(.el-textarea__inner) {
				height: 180px;
			}

			::v-deep(.el-table__expanded-cell) {
				padding: 20px;
			}

			.expand {
				margin: auto 0;
				width: 100%;
				height: 80%;

				.info {
					.photo {
						width: 70px;
						height: 70px;
						margin: auto;
						border-radius: 50%;
						background: #fff;
						overflow: hidden;
						transition: all .3s;

						img {
							width: 100%;
							height: 100%;
						}

					}

					.name {
						margin-top: 5px;
						color: #000000;
						text-align: center;
						font-size: 18px;
					}
				}

				.introduce {
					margin: 10px;

					.item {
						margin: 10px;

						p {
							display: inline;
							text-align: center;
						}
					}

					.email {
						display: flex;
						justify-content: center;
						margin: 10px;
					}

					.text {
						span {
							display: block;
							text-align: center;
							margin-bottom: 10px;
							margin-top: 5px;
						}

					}


				}

			}

		}
	}

}

.permission-card {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 65%;
	overflow: auto;
	//height: 90%;

	&::-webkit-scrollbar {
		width: 0;
	}

	.newUserForm {

		.item {
			margin: 10px;
			display: flex;
			justify-content: left;
			align-items: center;

			span {
				width: 80px;
				text-align: left;
			}

			.el-input {
				margin-left: 20px;

			}

			.el-select {
				width: 100%;
			}
		}

		.permission.item {
			margin: 0;
			display: flex;
			flex-direction: column;

			.permissionContainer {
				margin: 10px;
				width: 80%;

				.line {
					margin: 20px;

					.el-switch {
						margin-bottom: 15px;
					}


				}
			}
		}
	}
}

.shake-horizontal {
	color: #ea0707;
	animation: shake-horizontal 0.8s cubic-bezier(0.455, 0.030, 0.515, 0.955) both;
}

@keyframes shake-horizontal {
	0%,
	100% {
		transform: translateX(0);
	}
	10%,
	30%,
	50%,
	70% {
		transform: translateX(-10px);
	}
	20%,
	40%,
	60% {
		transform: translateX(10px);
	}
	80% {
		transform: translateX(8px);
	}
	90% {
		transform: translateX(-8px);
	}
}
</style>