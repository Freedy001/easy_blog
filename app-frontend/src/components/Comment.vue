<template>
	<div class="root" @click="tip=''">
		<div class="input-box">
			<input id="t1" type="text" v-model="comment.username" placeholder="Name">
			<input id="t2" type="text" v-model="comment.email" placeholder="Email">
		</div>
		<textarea :placeholder="myplaceholder==null?'What do you want to say...':myplaceholder" v-model="comment.content" class="textarea"></textarea>
		<div class="bottom">
			<el-popover
					placement="top"
					title="success"
					trigger="manual"
					content="评论发布成功😎😎!"
					v-model:visible="visible"
			>
				<template #reference>
					<button type="button" :class="{'el-button':true,'shake-horizontal':tip!==''}" @click.stop="submit">SUBMIT</button>
				</template>
			</el-popover>
			<div class="hint red">
				<span style="color: #ff268b">{{ tip }}</span>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import {defineEmit, defineProps, getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {useRoute} from "vue-router";
import {post} from "../http";
defineProps(['fatherCommentId','myplaceholder'])
defineEmit(['commentCB'])
const {proxy}: any = getCurrentInstance();
const router = useRoute();


let comment = reactive({
	articleId: '',
	username: '',
	email: '',
	content: ''
})
let tip = ref('')
let visible = ref(false)

async function submit() {
	if (comment.username == '' || comment.email == '' || comment.content === '') {
		tip.value = '内容不能为空哦~~ 😥😥'
	} else if (!comment.email.match('^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$')) {
		tip.value = '邮箱格式不正确~~ 😥😥'
	} else {
		const id = router.query.id;
		if (id) {
			const data={
				articleId: id,
				fatherCommentId:proxy.fatherCommentId,
				username: comment.username,
				email: comment.email,
				content: comment.content
			}
			const response = await post('/comment/publish', data)
			if (response.code == 200) {
				localStorage.setItem("username", comment.username)
				localStorage.setItem("email", comment.email)
				visible.value = true
				comment.content = ''
				proxy.$emit('commentCB',data)
			}
		}
	}
}

watch(visible, (val) => {
	if (val) {
		setTimeout(() => {
			visible.value = false;
		}, 1500)
	}
})
onMounted(() => {
	comment.username = localStorage.getItem("username");
	comment.email = localStorage.getItem("email");
})


</script>

<!--suppress CssInvalidPseudoSelector -->
<style scoped lang="scss">
.root {
	margin: 20px auto;
	width: 800px;
	font-size: 14px;
	font-family: eafont, Hiragino Sans GB, Hiragino Sans GB W3, Microsoft YaHei, WenQuanYi Micro Hei, sans-serif;
	border: 1px solid #eee;
	border-radius: 6px;
	padding: 15px 12px;
	transition: all .3s;
	display: flex;
	flex-direction: column;
	align-items: center;

	.input-box {
		display: flex;
		justify-content: center;
		margin: 10px;
		width: 100%;

		input {
			width: 50%;
			height: 38px;
			font-size: 14px;
			margin: 0 10px 0 10px;
			background: none;
			outline: none;
			border: none;
		}

		input:focus {
			border: none;
			outline: none;
		}
	}

	.textarea {
		margin: auto;
		width: 97%;
		height: 180px;
		resize: none;
	}

	.bottom {
		width: 100%;
		display: flex;
		align-items: center;
		padding: 10px 100px 0 100px;

		.hint {
			margin-left: 10px;
		}
	}
}
.shake-horizontal {
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