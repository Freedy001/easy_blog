<template>
	<div class="post">
		<div :style="{right:left?'none':0}" class="img-box" :class="addDarkClass()">
			<img style="border-radius: 5px;cursor: pointer" @click="handleJump" :src="loadResource(articleItem.articlePoster)"
			     alt="net error">
		</div>
		<div :style="{right:left?0:'none',left:left?'none':0}" class="info" :class="addDarkClass()">
			<div class="time">{{ articleItem.publishTime }}</div>
			<div class="title">
				<router-link class="title-content" @click="handleJump" tag="a" to="">{{ articleItem.title }}</router-link>
				<el-tooltip content="顶置" v-if="articleItem.articleStatus===4" placement="top" :effect="isDarkMode()?'dark':'light'">
					<img :src="isDarkMode()?overheadDark:overhead" alt="" style="width: 20px;height: 20px;margin-left: 10px;cursor: pointer">
				</el-tooltip>
			</div>
			<div class="describe">{{ articleItem.articleDesc }}</div>
			<div class="stuff">
				<el-tooltip content="分类" placement="top" :effect="isDarkMode()?'dark':'light'">
					<div>
						<svg class="icon" aria-hidden="true">
							<use xlink:href="#icon-category01"></use>
						</svg>
						<span>{{ articleItem.articleCategory }}</span>
					</div>
				</el-tooltip>
				<el-tooltip content="标签" placement="top" :effect="isDarkMode()?'dark':'light'">
					<div class="tag">
						<svg class="icon" aria-hidden="true">
							<use xlink:href="#icon-biaoqian"></use>
						</svg>
						<span v-for="(item,i) in articleItem.articleTags" :key="guid()">{{ item }}</span>
					</div>
				</el-tooltip>
			</div>
			<div class="stuff">
				<el-tooltip content="字数" placement="top" :effect="isDarkMode()?'dark':'light'">
					<div>
						<svg class="icon" aria-hidden="true">
							<use xlink:href="#icon-ziti1"></use>
						</svg>
						<span>{{ articleItem.wordNum }}</span>
					</div>
				</el-tooltip>
				<el-tooltip content="观看数" placement="top" :effect="isDarkMode()?'dark':'light'">
					<div>
						<svg class="icon" aria-hidden="true">
							<use xlink:href="#icon-huahanAPP-icon--"></use>
						</svg>
						<span>{{ articleItem.visitNum }}</span>
					</div>
				</el-tooltip>
				<el-tooltip content="喜欢数" placement="top" :effect="isDarkMode()?'dark':'light'">
					<div>
						<svg class="icon" aria-hidden="true">
							<use xlink:href="#icon-xihuan"></use>
						</svg>
						<span>{{ articleItem.likeNum }}</span>
					</div>
				</el-tooltip>
				<el-tooltip content="作者" placement="top" :effect="isDarkMode()?'dark':'light'">
					<div class="tag">
						<svg class="icon" aria-hidden="true">
							<use xlink:href="#icon-author"></use>
						</svg>
						<span>{{ articleItem.authorName }}</span>
					</div>
				</el-tooltip>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import {defineProps, getCurrentInstance, ref, watch} from "vue";
import {useRouter} from "vue-router";
import {useStore} from "vuex";
import {loadResource} from "../http";
import {addDarkClass, isDarkMode} from "../utils/common";
import overhead from '../assets/icon/overhead.svg'
import overheadDark from '../assets/icon/overhead-dark.svg'

defineProps(['articleItem', 'left'])
const {proxy}:any = getCurrentInstance();
const router = useRouter();

function handleJump() {
	router.push(`/article?id=${proxy.articleItem.id}`)
}

function guid() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
		let r = Math.random() * 16 | 0,
				v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	});
}

</script>

<style scoped lang="scss">
.post {
	margin-top: 100px;
	display: flex;
	position: relative;
	align-items: center;
	height: 440px;

	.img-box {
		width: 680px;
		height: 440px;
		z-index: 99;
		min-width: 500px;
		overflow: hidden;
		border: 1px solid #f3fafd;
		transition: all 0.3s ease;
		position: absolute;
		border-radius: 10px;

		img {
			display: inline-block;
			width: 100%;
			height: 100%;
			object-fit: cover;
			border-radius: 10px;
		}
		&:hover{
			transform: scale(1.01);
		}
	}

	.img-box.dark {
		border: 1px solid #2d2d2d;
	}

	.info {
		padding: 80px 100px 0 80px;
		margin: 0;
		border: 1px solid #eaeaea;
		border-radius: 10px;
		text-align: left;
		width: 550px;
		height: 400px;
		right: 0;
		position: absolute;
		box-sizing: border-box;

		.time {
			text-align: left;
			letter-spacing: .4px;
			box-sizing: border-box;
			word-spacing: 1px;
			color: #999;
			font-size: 12px;
		}

		.title {
			margin-top: 8px;
			word-break: break-all;
			overflow: hidden;

			a {
				font-size: 24px;
				line-height: 30px;
				cursor: pointer;
				text-decoration: none;
				color: black;
			}

			a:hover {
				cursor: pointer;
				text-decoration: none;
				background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 4'%3E%3Cpath fill='none' stroke='%23ff3300' d='M0 3.5c5 0 5-3 10-3s5 3 10 3 5-3 10-3 5 3 10 3'/%3E%3C/svg%3E") repeat-x 0 100%;
				background-size: 20px auto;
				animation: waveMove 1s infinite linear;
			}

			@keyframes waveMove {
				from {
					background-position: 0 100%;
				}
				to {
					background-position: -20px 100%;
				}
			}
		}

		.describe {
			margin-top: 10px;
			font-size: 14px;
			color: #666;
		}

		.stuff {
			position: absolute;
			margin-top: 5px;
			bottom: 80px;
			display: flex;
			justify-content: space-between;

			div {
				margin-right: 15px;
				cursor: pointer;

				.icon {
					margin-right: 5px;
					color: #98928f;
				}

			}

			.tag {
				span {
					margin-right: 10px;
				}
			}
		}

		.stuff:last-child {
			bottom: 30px;
		}
	}

	.info.dark {
		border: 1px solid #2d2d2d;

		.time {
			color: #999;
		}

		.title {
			a {
				color: #f3f3f3;
			}
		}

		.describe {
			margin-top: 10px;
			font-size: 14px;
			color: #a3a3a3;
		}
	}

}

</style>