<template>
	<FullScreen :opacity="0.5" :index="3000">
		<el-upload
				@click.stop=""
				class="upload-demo"
				drag
				:data="policyData"
				:before-upload="beforeUpload"
				:action="loadResource(actionUrl)"
				list-type="picture"
				:headers="token"
				:on-success="success"
				multiple>
			<i class="el-icon-upload"></i>
			<div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
		</el-upload>
	</FullScreen>
</template>

<script setup lang="ts">
import {ElMessage} from "element-plus";
import {get, loadResource} from "../http";
import {defineComponent, defineEmit, getCurrentInstance, reactive, ref} from "vue";
import FullScreen from "./FullScreen.vue";
import {copyProperties, UUID} from "../util/Common";
import {useStore} from "vuex";

defineComponent({
	FullScreen
})
defineEmit(['successCB'])
let token = {Authorization: localStorage.getItem('Authorization')}
const store = useStore();
const {proxy}: any = getCurrentInstance();
const mode: boolean = store.state.userInfo.uploadMode

let policyData = reactive<any>({});
let actionUrl = ref<string>('/backend/file/upload')
let filePath: string;

async function beforeUpload(file: any) {
	if (!mode) {
		token.Authorization = null;
		const response = await get('file////////getPolicy');
		if (response.code == 200) {
			console.log(file)
			const data = response.data;
			let fileName = UUID() + file.name;
			policyData.policy = data.policy
			policyData.signature = data.signature
			policyData.ossaccessKeyId = data.accessid
			filePath = "/"+data.dir + fileName
			policyData.key = data.dir + fileName
			policyData.dir = data.dir
			policyData.host = data.host
			actionUrl.value = data.host
			console.log(policyData)
			return true
		} else {
			ElMessage({
				showClose: true,
				message: `上传失败${response.msg}`,
				type: 'error'
			});
			return false;
		}
	} else {
		actionUrl.value = '/backend/file/upload'
		return true;
	}
}
//文件上传后的回调
async function success() {
	if (!mode) {
		const response = await get(`file/uploadSuccess?filePath=${filePath}`);
		if (response.code == 200) {
			ElMessage("上传成功！")
		} else {
			ElMessage.error(response.msg)
		}
	} else {
		ElMessage("上传成功！")
	}
	proxy.$emit('successCB')
}

</script>

<style scoped lang="scss">

.upload-demo {
	:deep(.el-upload-list) {
		width: 360px;
	}

	position: absolute;
	top: 10%;
	left: 50%;
	transform: translateX(-50%);
}
</style>