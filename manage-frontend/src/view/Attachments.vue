<template>
	<div class="root">
		<div class="btn-area">
			<div class="switch-btn">
				<el-switch
						style="display: block"
						v-model="switchValue"
						active-color="rgb(243 119 119)"
						inactive-color="rgb(11 191 226)"
						active-text="复制链接"
						inactive-text="操作图片"
				>
				</el-switch>
			</div>
			<transition name="el-fade-in-linear">
				<div class="ops-btn" v-if="!switchValue">
					<el-button type="primary" @click="showCard=true">上传</el-button>
					<el-button type="danger" @click="del">删除</el-button>
				</div>
			</transition>
		</div>
		<div class="img-area">
			<div class="item" @click="handleClick(url,i)" v-for="(url,i) in resource" :key='url'>
				<div class="checked">
					<img :src="check" alt="">
				</div>
				<el-image :key="url" :src="loadResource(url)" lazy></el-image>
			</div>
		</div>
		<teleport to="body">
			<transition name="el-fade-in-linear">
				<Upload v-if="showCard" @click="showCard=false" @successCB="success"></Upload>
			</transition>
		</teleport>
	</div>
</template>

<script setup lang="ts">
import {defineComponent, getCurrentInstance, h, onMounted, onUnmounted, reactive, ref, watch} from "vue";
import {get, loadResource, post} from "../http";
import LoadMore from '../components/LoadMore.vue'
import Upload from '../components/Upload.vue'
import check from '../assets/check.svg'
import {ElMessage} from "element-plus";

const {proxy}: any = getCurrentInstance();
defineComponent({
	LoadMore,
	Upload
})
let interval: number;
let pre = new Date().getTime()
onMounted(() => {
	const item = localStorage.getItem("switchValue");
	if (item) {
		switchValue.value = item === '0';
	}
	getImageUrls().then(() => {
		setTimeout(() => {
			waterFall()
			const content: any = document.querySelector("#content");
			window.onresize = () => waterFall();
			content.onscroll = (event: any) => {
				if (new Date().getTime() - pre > 50) {
					doScroll(event)
				}
			};
		}, 500)
		interval = setInterval(() => {
			waterFall()
		}, 1000);
	})
})
onUnmounted(() => {
	clearInterval(interval)
})
//滚动事件
let hasMore = ref(true)
let doScroll: any = ({srcElement}: any) => {
	if ((srcElement.scrollTop + srcElement.clientHeight > srcElement.scrollHeight - 50) && hasMore.value) {
		page++;
		getImageUrls()
	}
}

let showCard = ref(false)
watch(showCard,(val)=>{
	if (!val){
		resource.length = 0
		page = 1;
		getImageUrls().then();
	}
})
// setInterval(()=>{
// 	ElMessage("aaaa")
// },500)
//文件上传后的回调
async function success() {

}

let SelectedList: any = [];
let hasSelect = ref(false)
let switchValue = ref(false)
onUnmounted(() => {
	localStorage.setItem("switchValue", switchValue.value ? '0' : '1');
})

//点击图片
function handleClick(url: string, index: number) {
	if (switchValue.value) {
		try {
			let oInput = document.createElement('input');
			oInput.value = loadResource(converter(resource[index]));
			oInput.style.opacity = '0'
			oInput.setAttribute("name", "secukey")
			document.body.appendChild(oInput);
			oInput.select(); // 选择对象
			document.execCommand("Copy"); // 执行浏览器复制命令
			oInput.id = 'oInput';
			oInput.remove()
			ElMessage.success({
				message: '复制成功!',
				type: 'success'
			});
		} catch (e) {
			console.log(e)
			ElMessage.error('url转换失败!');
		}
	} else {
		const ele: any = document.getElementsByClassName("item")[index];
		const check: any = document.getElementsByClassName("checked")[index];
		let picIndex;
		if ((picIndex = SelectedList.indexOf(index)) == -1) {
			SelectedList.push(index)
			ele.classList.add('select')
			check.style.display = 'block'
			hasSelect.value = true;
		} else {
			SelectedList.splice(picIndex, 1);
			ele.classList.remove('select')
			check.style.display = 'none'
			hasSelect.value = SelectedList.length > 0
		}
	}
}

function converter(url: string) {
	if (url.match(/\?x-oss-process/)) {
		return url.split("\?x-oss-process")[0];
	}
	if (url.match(/\/image\//)) {
		let split = url.split("-", 5);
		return split[0] + "-" + split[1] + "-" + split[2] + "-" + split[4];
	}
	throw new Error('转换失败');
}

//删除图片
async function del() {
	let urlList: any = []
	SelectedList.forEach((index: any) => urlList.push(resource[index]))
	const response = await post('/file/delPic', urlList);
	if (response.code == 200) {
		proxy.$notify({
			message: response.data,
		})
		//取消选中
		const ele: any = document.getElementsByClassName("item");
		const check: any = document.getElementsByClassName("checked");
		SelectedList.forEach((index: any) => {
			ele[index].classList.remove('select')
			check[index].style.display = 'none'
		})
		SelectedList = [];
		resource.length = 0
		page = 1;
		getImageUrls().then();
	} else {
		proxy.$notify.error({
			title: '出差啦😢！',
			message: response.msg,
			duration: 5000
		})
	}
}

let page = 1;
let resource = reactive<Array<string>>([])

//加载图片url
async function getImageUrls() {
	const response = await get(`/file/getImages?page=${page}&limit=30&sidx=id&order=desc`);
	if (response.code == 200) {
		const list: Array<any> = response.data.list
		if (list.length == 0) {
			hasMore.value = false
		}
		list.forEach((value, index) => {
			resource.push(value.resourceUrl)
		})
	}
}


function waterFall() {
	let items: any = document.getElementsByClassName('item');
	if (items.length === 0) return;
	let gap: any = 10
	//首先确定列数 = 页面的宽度 / 图片的宽度
	const content: any = document.querySelector("#content");
	let pageWidth: any = content.clientWidth - 50;
	let itemWidth: any = items[0].offsetWidth;
	let columns: any = parseInt(String(pageWidth / (itemWidth + gap)));
	let arr: any = [];//定义一个数组，用来存储元素的高度
	for (let i = 0; i < items.length; i++) {
		if (i < columns) {
			//满足这个条件则说明在第一行，文章里面有提到
			items[i].style.top = 0;
			items[i].style.left = (itemWidth + gap) * i + 'px';
			arr.push(items[i].offsetHeight);
		} else {
			//其他行，先找出最小高度列，和索引
			//假设最小高度是第一个元素
			let minHeight = arr[0];
			let index = 0;
			for (let j = 0; j < arr.length; j++) {//找出最小高度
				if (minHeight > arr[j]) {
					minHeight = arr[j];
					index = j;
				}
			}
			//设置下一行的第一个盒子的位置
			//top值就是最小列的高度+gap
			items[i].style.top = arr[index] + gap + 'px';
			items[i].style.left = items[index].offsetLeft + 'px';

			//修改最小列的高度
			//最小列的高度 = 当前自己的高度 + 拼接过来的高度 + 间隙的高度
			arr[index] = arr[index] + items[i].offsetHeight + gap;
		}
	}
}

</script>

<style scoped lang="scss">
.root {
	.btn-area {
		width: 100%;
		display: flex;
		justify-content: space-between;
		height: 65px;

		.switch-btn {
			height: 100%;
			display: flex;
			align-items: center;

			.el-switch {
				margin-bottom: 10px;

				:deep(span) {
					font-size: 15px;
				}

			}
		}

		.el-button {
			margin: 10px;
		}
	}

	.img-area {
		position: relative;

		.item {
			position: absolute;
			width: 300px;
			border-radius: 10px;
			transition: all .3s ease;
			cursor: pointer;

			.checked {
				position: absolute;
				top: 3%;
				left: 3%;
				display: none;
				z-index: 999;

				img {
					width: 35px;
					border: none;
					background-color: rgba(255, 255, 255, 0);
				}
			}

			.el-image {
				width: 100%;
				border-radius: 10px;
				background-color: #0b9aff;
			}

		}

		.select {
			.el-image {
				border: 1px solid #0b9aff;
			}

			transform: scale(1.03);
		}
	}

	.upload-demo {
		position: absolute;
		top: 15%;
		left: 52%;
		transform: translate(-50%);
		width: 30%;
	}
}
</style>