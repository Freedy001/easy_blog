<template>
	<div class="root" :class="addDarkClass()" @click="tip=''">
		<div class="input-box" :class="addDarkClass()">
			<input autocomplete="off" id="t1" type="text" v-model="comment.username" placeholder="Name">
			<input autocomplete="off" id="t2" type="text" v-model="comment.email" placeholder="Email">
		</div>
		<textarea :class="addDarkClass()" :placeholder="myplaceholder==null?'What do you want to say...':myplaceholder"
		          v-model="comment.content" class="textarea"></textarea>
		<div class="bottom">
			<el-popover
					placement="top"
					title="success"
					trigger="manual"
					:content="store.state.indexSetting.examination?'评论成功请耐心等待管理员审核哦！':'评论发布成功😎!'"
					v-model:visible="visible"
			>
				<template #reference>
					<button type="button"
					        :class="{'el-button':true,'shake-horizontal':tip!=='','button-dark':store.state.darkMode}"
					        @click.stop="submit">SUBMIT
					</button>
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
import {addDarkClass} from "../utils/common";
import {useStore} from "vuex";

defineProps(['fatherCommentId', 'myplaceholder'])
defineEmit(['commentCB'])
const {proxy}: any = getCurrentInstance();
const router = useRoute();
const store = useStore();

let comment = reactive<any>({
	articleId: '',
	username: '',
	email: '',
	content: ''
})
let tip = ref('')
let visible = ref(false)

async function submit() {
	let pattern=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	if (comment.username == '' || comment.email == '' || comment.content === '') {
		tip.value = '内容不能为空哦~~ 😥😥'
	} else if (!pattern.test(comment.email)) {
		tip.value = '邮箱格式不正确~~ 😥😥'
	} else {
		const id = router.query.id;
		if (id) {
			const data = {
				articleId: id,
				fatherCommentId: proxy.fatherCommentId,
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
				proxy.$emit('commentCB', data)
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
	border: 1px solid #474747;
	border-radius: 10px;
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
			margin: 0 10px 0 10px;
			border: 2px solid #e8e8e8;
			border-radius: 5px;
			transition: all .3s ease;
			background: none;
			outline: none;
			text-indent: 16px;
			font-size: 15px;

			&::-webkit-input-placeholder {
				color: #a1a1a1;
				font-size: 15px;
				text-indent: 16px
			}
		}

		input:focus {
			outline: none;
			margin: 0 10px 0 10px;
			border: 2px solid #0b9aff;
		}
	}

	.textarea {
		margin: auto;
		width: 97%;
		height: 180px;
		resize: none;
		outline: none;
		border: 2px solid #e8e8e8;
		border-radius: 5px;
		transition: all .3s ease;
		text-indent: 20px;
		font-size: 16px;
		line-height: 25px;

		&:focus {
			border: 2px solid #0b9aff;
		}

		&::-webkit-input-placeholder {
			color: #a1a1a1;
			font-size: 18px;
			text-indent: 16px
		}
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

.root.dark {
	border: 1px solid #3b3b3b;

	.input-box.dark {
		input {
			border: 2px solid #454545;
			color: #eeeeee;

			&::-webkit-input-placeholder {
				color: #d5d5d5;
				font-size: 15px;
				text-indent: 16px
			}
		}

		input:focus {
			border: 2px solid #2f4368;
		}
	}

	.textarea.dark {
		color: #eeeeee;
		border: 2px solid #454545;
		background-color: #0d1117;

		&:focus {
			border: 2px solid #2f4368;
		}

		&::-webkit-input-placeholder {
			color: #d5d5d5;
			font-size: 18px;
			text-indent: 16px
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

.el-button {
	margin-left: 10px;
	transition: all .3s ease;

	&:hover {
		background-color: #e8e8e8;
	}
}

.button-dark {
	color: #c4c4c4;
	background-color: #0d1117;

	&:hover {
		background-color: #273753;
	}
}

</style>