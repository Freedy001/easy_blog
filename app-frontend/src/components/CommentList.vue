<template>
	<div style="transition: all 0.5s ease;">
		<div class="container">
			<div class="header-img">
				<img :src="loadResource(`/resource/Boy-0${new Date().getTime()%5+1}.svg`)" alt="">
			</div>
			<div class="info">
				<span class="name">{{ commentItem.username }}</span>
				<p class="content">{{ commentItem.content }}</p>
				<div class="time">
					<span>{{ commentItem.createTime }}</span>
					<span class="replay" @click="handleReplay(commentItem.id,commentItem.username)">回复</span>
				</div>
			</div>
		</div>
		<div class="children-info" v-for="item in commentItem.childComment">
			<div class="content">
				<p>
					<span>{{ item.username }}:</span>
					<span v-if="item.parentName!==null&&item.parentName!==commentItem.username">回复
						<span class="replay-to">@{{item.parentName}}</span>
					</span>
					{{ item.content }}
				</p>
			</div>
			<div class="time">
				<span>{{ item.createTime }}</span>
				<span class="replay" @click="handleReplay(item.id,item.username)">回复</span>
			</div>
			<el-divider></el-divider>
		</div>
		<transition enter-active-class="scale-in-tr" leave-active-class="scale-out-left">
			<div ref="commentRef">
				<Comment v-if="showBox" :myplaceholder="myplaceholder" @commentCB="handleCommentSuccess"
				         :fatherCommentId="fatherCommentId"></Comment>
			</div>
		</transition>
		<el-divider></el-divider>
	</div>
</template>

<script setup lang="ts">
import Comment from './Comment.vue'
import {defineComponent, defineEmit, defineProps, getCurrentInstance, reactive, ref} from "vue";
import {loadResource} from "../http";

defineComponent({
	Comment
})
defineProps(['commentItem'])
defineEmit(['commentCB'])
const {proxy}:any = getCurrentInstance();

let showBox = ref(false)
/**
 *  处理评论成功的回调
 */
async function handleCommentSuccess(data:any) {
	showBox.value = false;
	setTimeout(() => {
		//等待动画执行完重新加载
		proxy.$emit('commentCB',data)
	}, 600)
}

let fatherCommentId = ref()
let myplaceholder = ref()
let cid:any;
let commentRef = ref(null)
/**
 * 处理点击回复按钮
 */
async function handleReplay(id: number, username: string) {
	if (cid == id) {
		showBox.value = !showBox.value;
	} else {
		showBox.value = true
	}
	cid = id;
	fatherCommentId.value = id
	myplaceholder.value = "回复 @" + username + ":"
	setTimeout(() => {
		if (showBox.value) {
			const element: any = commentRef.value;
			window.scrollTo({
				top: element.offsetTop - element.clientHeight,
				behavior: "smooth"
			});
		}
	}, 500)
}
</script>

<style scoped lang="scss">
.container {
	margin: 20px auto;
	width: 850px;
	display: flex;
	justify-content: space-between;

	.header-img {
		flex-shrink: 0;
		flex-grow: 0;
		width: 50px;
		height: 50px;
		margin-right: 35px;
		border-radius: 50%;
		overflow: hidden;

		img {
			width: 100%;
			height: 100%;
			object-fit: cover;
		}
	}

	.info {
		width: 100%;
		display: flex;
		flex-direction: column;

		span {
			font-size: 12px;
			margin: 5px;
		}

		.name {
			color: #6d757a;
			font-size: 12px;
		}

		.content {
			margin: 5px;
			line-height: 20px;
			font-size: 14px;
			text-shadow: none;
			overflow: hidden;
		}

		.time {
			color: #99a2aa;
			font-size: 12px;

			.replay {
				cursor: pointer;
				padding: 5px;
				border-radius: 5px;
				transition: all 0.3s ease
			}

			.replay:hover {
				background-color: #d7d7d7;
				color: #00a1d6;
			}
		}
	}
}

.children-info {
	margin: 20px auto;
	width: 680px;
	display: flex;
	justify-content: space-between;
	flex-direction: column;
	overflow: hidden;

	.content {
		display: flex;
		align-items: center;
		margin-bottom: 5px;


		span {
			position: relative;
			top: 0;
			margin: 0 5px 5px 0;
			color: #FB7299;
			font-size: 12px;
			border-radius: 5px;
		}

		p {
			margin: 5px;
			line-height: 20px;
			font-size: 14px;
			text-shadow: none;
			overflow: hidden;

			.replay-to {
				cursor: pointer;
				color: #00a1d6;
			}
		}
	}

	.time {
		color: #99a2aa;
		font-size: 12px;

		.replay {
			margin-left: 5px;
			cursor: pointer;
			padding: 5px;
			border-radius: 5px;
			transition: all 0.3s ease
		}

		.replay:hover {
			background-color: #d7d7d7;
			color: #00a1d6;
		}
	}

	.el-divider {
		margin-top: 5px;
	}
}

.el-divider {
	margin: auto;
	width: 900px;
	background-color: rgba(232, 232, 232, 0.51);
}

.scale-in-tr {
	animation: scale-in-tr 0.5s cubic-bezier(0.250, 0.460, 0.450, 0.940) both;
}

@keyframes scale-in-tr {
	0% {
		transform: scale(0);
		transform-origin: 100% 0;
		opacity: 1;
	}
	100% {
		transform: scale(1);
		transform-origin: 100% 0;
		opacity: 1;
	}
}

.scale-out-left {
	animation: scale-out-left 0.5s cubic-bezier(0.550, 0.085, 0.680, 0.530) both;
}

@keyframes scale-out-left {
	0% {
		transform: scale(1);
		transform-origin: 100% 0;
		opacity: 1;
	}
	100% {
		transform: scale(0);
		transform-origin: 100% 0;
		opacity: 1;
	}
}
</style>
