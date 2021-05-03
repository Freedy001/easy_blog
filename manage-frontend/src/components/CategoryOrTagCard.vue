<template>
	<div class="card-row">
		<el-card class="box-card form-area">
			<div>
				<h1>{{ name }}</h1>
				<br>
				<el-form :model="form">
					<el-form-item label="名称">
						<el-input type="text"
						          v-model="form.name"
						          placeholder="请输入标签名称"
						          maxlength="10"
						          show-word-limit
						></el-input>
					</el-form-item>
					<el-form-item label="图片url">
						<el-input type="text"
						          v-model="form.url"
						          placeholder="请输入图片url"
						></el-input>
					</el-form-item>
					<el-form-item label="优先级">
						<el-select v-model="form.priority" placeholder="请选择标签优先级">
							<el-option label="最高" :value="1"></el-option>
							<el-option label="高" :value="2"></el-option>
							<el-option label="普通" :value="3"></el-option>
							<el-option label="低" :value="4"></el-option>
							<el-option label="最低" :value="5"></el-option>
						</el-select>
					</el-form-item>
				</el-form>
			</div>
			<div class="button-area">
				<div>
					<el-button type="primary" round @click="insertOrUpdate">
						{{ showBtn ? '修改' : '添加' }}
					</el-button>
					<el-button v-if="showBtn" round @click="returnBtn">返回</el-button>
				</div>
				<el-button v-if="showBtn" type="danger" round @click="deleteItem">删除</el-button>
			</div>
		</el-card>
		<el-card class="box-card tag-area">
			<h1>所有{{ name }}</h1>
			<br>
			<el-check-tag v-for="(item,i) in tagList" :key="i" @change="onChange(i)" :checked="checked[i]">
				{{ item.name }}
			</el-check-tag>
		</el-card>
	</div>
</template>

<script setup lang="ts">
import {
	computed,
	defineComponent,
	defineEmit,
	defineProps,
	getCurrentInstance,
	onMounted,
	reactive,
	ref,
	watch
} from "vue";
import {useStore} from "vuex";
import {get, post} from "../http";
import {ElMessage} from "element-plus";
defineProps(['name'])
defineEmit(['callback'])
const {proxy} = getCurrentInstance();
//按钮显示
let showBtn = ref(false)
//点击标签事件
let checked = reactive([])
//表单数据
let form = reactive({
	id:'',
	name: '',
	url: '',
	priority: '',
})
//标签集合---服务器返回的数据
interface tag{
	id: number,
	name: string,
	url: string,
	priority: number,
}
let tagList = reactive<Array<tag>>([{
	id: 0,
	name: '',
	url: '',
	priority: 5,
}])
//*************form*****************
function returnBtn() {
	for (let i = 0; i < checked.length; i++) {
		checked[i] = false
	}
	showBtn.value = false
	cleanForm()
}

/**
 * 添加标签或者删除标签
 */
async function insertOrUpdate(){
	let data;
	if (proxy.name=='标签'){
		data={
			id:form.id,
			tagName:form.name,
			tagImgUrl:form.url,
			priority:form.priority
		}
	}else {
		data={
			id:form.id,
			categoryName:form.name,
			categoryImgUrl:form.url,
			priority:form.priority
		}
	}
	if (showBtn.value){
		//更新
		const response = await post(`/${proxy.name=='分类'?'category':'tag'}/update`,data);
		if(response.code==200){
			proxy.$emit('callback')
			loadTag().then()
			proxy.$notify({
				title: '成功',
				message: '更新成功!',
				type: 'success'
			})
		}else {
			proxy.$notify.error({
				title: '错误',
				message: `更新失败！ reason-->${response.msg}`
			})
		}
	}else {
		//添加
		data.id=null;//清空id 防止干扰
		const response = await post(`/${proxy.name=='分类'?'category':'tag'}/save?`,data);
		if(response.code==200){
			proxy.$emit('callback')
			loadTag().then()
			proxy.$notify({
				title: '成功',
				message: '保存成功!',
				type: 'success'
			})
		}else {
			proxy.$notify.error({
				title: '错误',
				message: `保存失败！ reason-->${response.msg}`
			})
		}
	}
	cleanForm()
}

async function deleteItem() {
	const response = await get(`/${proxy.name=='分类'?'category':'tag'}/delete?ids=${form.id}`);
	if(response.code==200){
		loadTag().then()
		proxy.$notify({
			title: '成功',
			message: `删除成功!`,
			type: 'success'
		})
	}else {
		proxy.$notify.error({
			title: '错误',
			message: `删除失败！ reason-->${response.msg}`
		})
	}
	cleanForm()
}

//清空表单
function cleanForm() {
	form.id=''
	form.name = ''
	form.url = ''
	form.priority = ''
}

/**
 * 点击某个标签时
 * 对表单进行回显
 */
function onChange(index) {
	for (let i = 0; i < checked.length; i++) {
		checked[i] = false
	}
	checked[index] = true
	showBtn.value = true;
	form.id = tagList[index].id
	form.name = tagList[index].name
	form.url = tagList[index].url
	form.priority = tagList[index].priority
}
//初始化页面的时候加载
onMounted( () => {
	loadTag().then()
})
//加载标签
async function loadTag() {
	const data = await get(`/${proxy.name=='分类'?'category':'tag'}/list?page=1&limit=50&sidx=priority&order=asc`);
	if (data.code == 200) {
		tagList.length=0;
		let arr: Array<any> = data.page.list
		arr.forEach((value, index) => {
			tagList.push({
				id: value.id,
				name: proxy.name=='分类'?value.categoryName:value.tagName,
				url: proxy.name=='分类'?value.categoryImgUrl:value.tagImgUrl,
				priority: value.priority
			})
		})
		for (let i = 0; i < arr.length; i++) {
			checked[i] = false
		}
	} else {
		ElMessage({
			showClose: true,
			message: `网络异常！${data.msg}`,
			type: 'error'
		});
	}
}
</script>

<style scoped lang="scss">
.card-row {
	display: flex;
	justify-content: space-between;
	height: 45%;
}

.box-card {
	width: 49%;
	overflow: auto;

	h1 {
		color: #0b9aff;
		font-size: 20px;
		font-weight: lighter;
	}

	.el-form {
		padding: auto 0;

		.el-form-item {
			display: flex;
			justify-content: space-between;

			::v-deep(.el-form-item__label) {
				padding: 0;
				width: 20px;
				flex-grow: 1;
				text-align: left;
			}

			::v-deep(.el-form-item__content) {
				flex-grow: 1;
				width: 300px;
			}
		}
	}

	.el-check-tag {
		margin: 5px;
	}

	.button-area {
		display: flex;
		justify-content: space-between;
	}
}

::v-deep(.form-area .el-card__body) {
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
}
</style>