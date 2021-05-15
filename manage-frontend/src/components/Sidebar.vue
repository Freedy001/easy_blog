<template>
	<div>
		<div class="sidebar" :class="{hide:scale}">
			<div class="info">
				<el-dropdown>
					<div class="photo" style="cursor:pointer;">
						<img src="src/assets/login-0.png">
					</div>
					<template #dropdown>
						<el-dropdown-menu>
							<el-dropdown-item @click="$router.push('/index/user')"><i class="el-icon-user-solid" style="margin-right: 10px"></i>个人资料</el-dropdown-item>
							<el-dropdown-item @click="lg"><i class="el-icon-more-outline" style="margin-right: 10px"></i>退出登录</el-dropdown-item>
						</el-dropdown-menu>
					</template>
				</el-dropdown>
				<p class="name">Freedy</p>
			</div>
			<el-menu
					mode="horizontal"
					:default-active="$router.currentRoute.value.path"
					router
			>
				<el-menu-item index="/index">
					<i class="el-icon-help"></i>
					<span>仪表盘</span>
				</el-menu-item>
				<div class="divide-line"></div>
				<el-menu-item index="/index/article">
					<i class="el-icon-help"></i>
					<span>发布文章</span>
				</el-menu-item>
				<el-menu-item index="/index/articleList">
					<i class="el-icon-help"></i>
					<span>文章列表</span>
				</el-menu-item>
				<el-menu-item index="/index/category&tag">
					<i class="el-icon-help"></i>
					<span>分类与标签</span>
				</el-menu-item>
				<el-menu-item index="/index/comment">
					<i class="el-icon-help"></i>
					<span>评论</span>
					<span>2</span>
				</el-menu-item>
				<el-menu-item index="/index/user">
					<i class="el-icon-help"></i>
					<span>用户</span>
				</el-menu-item>
				<div class="divide-line"></div>
				<el-menu-item index="/index/setting">
					<i class="el-icon-help"></i>
					<span>设置</span>
				</el-menu-item>
			</el-menu>
			<transition name="fade" mode="out-in">
				<span v-if="scale" @click="scale=!scale" class="el-icon-open"></span>
				<span v-else @click="scale=!scale" class="el-icon-turn-off"></span>
			</transition>
		</div>
	</div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {logout} from'../http'
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";
const scale = ref(false)
const router = useRouter();

async function lg() {
	await logout()
	await router.push('/login')
}

</script>

<style scoped lang="scss">
.fade-enter-active {
	transition: all .3s ease;
}

.fade-leave-active {
	transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.fade-enter, .fade-leave-to
	/* .slide-fade-leave-active for below version 2.1.8 */
{
	opacity: 0;
}

.sidebar {
	height: 100%;
	width: 240px;
	color: #fff;
	padding-top: 40px;
	background: #0e8bff;
	transition: all .3s;
	position: relative;

	.el-menu {
		border: none;
		margin-top: 40px;
		background: none;
		padding-left: 8px;

		.divide-line {
			height: 2px;
			width: 100%;
			margin-top: 10px;
			margin-bottom: 10px;
			background: linear-gradient(30deg, #f8f8f8, #00b9ff, #3a9ff5, #ffff00);
			background-size: 1400% 300%;
			animation: mymove 3s ease-in-out infinite alternate;
		}

		@keyframes mymove {
			0% {
				background-position: 0% 0%;
			}
			50% {
				background-position: 50% 100%;
			}
			100% {
				background-position: 100% 0%;
			}
		}


		.el-menu-item {
			height: 40px;
			line-height: 42px;
			float: none;
			border: none;
			color: #fff;
			font-size: 14px;
			margin-bottom: 5px;
			padding: 0 0 0 32px;
			text-align: left;
			border-radius: 20px 0 0 20px;
			transition: all .3s;

			i {
				color: #fff;
				vertical-align: middle;
			}

			&:focus {
				color: #fff;
				background-color: #0e8bff;
			}

			&:hover, &.is-active {
				color: #0084ff;
				background: #fff;
				position: static;

				i {
					color: #0084ff;
				}
			}

		}
	}

	.info {
		text-align: center;

		.photo {
			width: 70px;
			height: 70px;
			margin: auto;
			border-radius: 50%;
			background: #fff;
			overflow: hidden;
			transition: all .3s;
			border: 5px solid #389fff;

			img {
				width: 100%;
				height: 100%;
			}
		}

		.name {
			margin-top: 15px;
			color: #fff;
		}
	}

	&.hide {
		width: 80px;

		.info {
			.photo {
				width: 50px;
				height: 50px;
			}

			.name {
				display: none;
			}
		}

		.el-menu {
			padding: 0 8px;
			text-align: center;

			.el-menu-item {
				border-radius: 6px;
				padding: 0;
				text-align: center;
				width: 48px;
				line-height: 42px;
				height: 42px;
				padding-left: 2px;
				display: inline-block;

				i {
					font-size: 26px;
				}

				span {
					display: none;
				}

				&.is-active::before {
					content: '';
					height: 24px;
					width: 3px;
					position: absolute;
					left: 0;
					z-index: 999;
					background: #fff;
					transform: translateY(8px);
					border-radius: 0 15px 15px 0;
				}
			}
		}
	}

	.el-icon-open, .el-icon-turn-off {
		font-size: 20px;
		position: absolute;
		bottom: 10px;
		left: 30px;
		cursor: pointer;
		color: #83c3ff;

		&:hover {
			color: #fff;
		}

		&.el-icon-turn-off {
			left: 50%;
			transform: translateX(-50%);
		}
	}
}

.misk {
	position: relative;
	width: 100%;
	height: 100%;
	z-index: 999;
	background: rgba(127, 127, 127, 0);
	display: none;
}

.btn {
	position: fixed;
	bottom: 20px;
	right: 30px;
	background: #c6e1ff;
	height: 40px;
	width: 40px;
	border-radius: 50%;
	text-align: center;
	line-height: 40px;
	color: #0084ff;
	font-size: 24px;
	z-index: 999999;
	display: none;

	&.el-icon-close {
		font-size: 20px;
		line-height: 42px;
	}
}
</style>