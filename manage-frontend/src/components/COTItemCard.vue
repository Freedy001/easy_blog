<template>
	<el-card class="box-card tag-area">
		<h1>所有{{ name }}</h1>
		<br>
		<el-check-tag v-for="(item,i) in nameList" :key="i" @change="onChange(item,i)" :checked="checked[i]">
			{{item.name}}
		</el-check-tag>
	</el-card>
</template>

<script setup lang="ts">
import {computed, defineProps, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {useStore} from "vuex";
import {get} from '../http'
import {ElMessage} from "element-plus";
const store = useStore();
const {proxy} = getCurrentInstance();
defineProps(['name'])
//点击标签事件
let checked = computed(()=>reactive(store.state.COT.checked))

function onChange(data,index) {
	store.commit('clickLabel',{
		data:data,
		index:index,
		name:proxy.name
	})
}
let nameList=reactive([{
	id:1,
	name:'blank',
	url:'blank',
	priority:1,
},{
	id:2,
	name:'blank',
	url:'blank',
	priority:1,
}])
//加载标签
onMounted(async ()=>{
	if (proxy.name=='分类'){
		const data =await get(`/category/list?page=1&limit=50&sidx=priority&order=asc`);
		if (data.code==200){
			let arr:Array<any>=data.page.list
			arr.forEach((value, index) => {
				nameList[index]= {
					id:value.id,
					name:value.categoryName,
					url:value.categoryImgUrl,
					priority:value.priority
				}
			})
			store.commit('initChecked',arr.length)
		}else {
				ElMessage({
					showClose: true,
					message: `网络异常！${data.msg}`,
					type: 'error'
				});
		}
	}else if (proxy.name=='标签'){
		const data =await get(`/category/list?page=1&limit=50&sidx=priority&order=asc`);
		if (data.code==200){
			let arr:Array<any>=data.page.list
			arr.forEach((value, index) => {
				nameList[index]=value.categoryName
			})
		}else {
			ElMessage({
				showClose: true,
				message: `网络异常！${data.msg}`,
				type: 'error'
			});
		}
	}

})
</script>

<style scoped lang="scss">
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
</style>