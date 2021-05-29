<!--<template>-->
<!--	<el-card class="box-card form-area">-->
<!--		<div>-->
<!--			<h1>{{ name }}</h1>-->
<!--			<br>-->
<!--			<el-form :model="form">-->
<!--				<el-form-item label="名称">-->
<!--					<el-input type="text"-->
<!--					          v-model="form.name"-->
<!--					          placeholder="请输入标签名称"-->
<!--					          maxlength="10"-->
<!--					          show-word-limit-->
<!--					></el-input>-->
<!--				</el-form-item>-->
<!--&lt;!&ndash;				<el-form-item label="图片url">&ndash;&gt;-->
<!--&lt;!&ndash;					<el-input type="text"&ndash;&gt;-->
<!--&lt;!&ndash;					          v-model="form.url"&ndash;&gt;-->
<!--&lt;!&ndash;					          placeholder="请输入图片url"&ndash;&gt;-->
<!--&lt;!&ndash;					></el-input>&ndash;&gt;-->
<!--&lt;!&ndash;				</el-form-item>&ndash;&gt;-->
<!--				<el-form-item label="优先级">-->
<!--					<el-select v-model="form.priority" placeholder="请选择标签优先级">-->
<!--						<el-option label="最高" value="1"></el-option>-->
<!--						<el-option label="高" value="2"></el-option>-->
<!--						<el-option label="普通" value="3"></el-option>-->
<!--						<el-option label="低" value="4"></el-option>-->
<!--						<el-option label="最低" value="5"></el-option>-->
<!--					</el-select>-->
<!--				</el-form-item>-->
<!--			</el-form>-->
<!--		</div>-->
<!--		<div class="button-area">-->
<!--			<div>-->
<!--				<el-button type="primary" round @click="$emit('add',form)">-->
<!--					{{showBtn?'修改':'添加'}}-->
<!--				</el-button>-->
<!--				<el-button v-if="showBtn" round @click="returnBtn">返回</el-button>-->
<!--			</div>-->
<!--			<el-button v-if="showBtn" type="danger" round @click="$emit('del')">删除</el-button>-->
<!--		</div>-->
<!--	</el-card>-->
<!--</template>-->

<!--<script setup lang="ts">-->
<!--import {computed, defineEmit, defineProps, getCurrentInstance, reactive, ref, watch} from "vue";-->
<!--import {useStore} from "vuex";-->
<!--const store = useStore();-->
<!--defineProps(['name'])-->
<!--defineEmit(['add', 'return', 'del'])-->
<!--let showBtn=computed(()=>store.state.COT.showBtn)-->
<!--let form = reactive({-->
<!--	name: '',-->
<!--	url: '',-->
<!--	priority: '',-->
<!--})-->
<!--function returnBtn(){-->
<!--	store.commit('clickReturn')-->
<!--	form.name=''-->
<!--	form.url=''-->
<!--	form.priority=''-->
<!--}-->
<!--const {proxy}:any = getCurrentInstance();-->
<!--watch(()=>store.state.COT.form,()=>{-->
<!--	if (proxy.name){-->
<!--		const data=store.state.COT.form-->
<!--		form.name=data.name-->
<!--		form.url=data.url-->
<!--		form.priority=data.priority-->
<!--	}-->
<!--})-->




<!--</script>-->

<!--<style scoped lang="scss">-->
<!--.box-card {-->
<!--	width: 49%;-->
<!--	overflow: auto;-->

<!--	h1 {-->
<!--		color: #0b9aff;-->
<!--		font-size: 20px;-->
<!--		font-weight: lighter;-->
<!--	}-->

<!--	.el-form {-->
<!--		padding: auto 0;-->

<!--		.el-form-item {-->
<!--			display: flex;-->
<!--			justify-content: space-between;-->

<!--			:deep(.el-form-item__label) {-->
<!--				padding: 0;-->
<!--				width: 20px;-->
<!--				flex-grow: 1;-->
<!--				text-align: left;-->
<!--			}-->

<!--			:deep(.el-form-item__content) {-->
<!--				flex-grow: 1;-->
<!--				width: 300px;-->
<!--			}-->
<!--		}-->
<!--	}-->

<!--	.el-check-tag {-->
<!--		margin: 5px;-->
<!--	}-->

<!--	.button-area {-->
<!--		display: flex;-->
<!--		justify-content: space-between;-->
<!--	}-->
<!--}-->

<!--:deep(.form-area .el-card__body) {-->
<!--	height: 100%;-->
<!--	display: flex;-->
<!--	flex-direction: column;-->
<!--	justify-content: space-between;-->
<!--}-->
<!--</style>-->
