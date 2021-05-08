<template>
	<div class="index-cover">
		<el-image
				src="https://cdn.pixabay.com/photo/2014/05/02/23/46/bridge-336475_1280.jpg"
				:fit="cover"></el-image>
		<div class="triangle"></div>
		<div class="logo-and-menu">
			<img style="margin-left: 30px" :src="imgUrl" alt="">
			<svg class="icon" aria-hidden="true">
				<use xlink:href="#icon-menu1"></use>
			</svg>
		</div>
		<div class="cover-title">
			<div class="time">一月 23, 2020</div>
			<div class="title">
				<a href="/#" data-v-241ea8f0="" class="title-a">要么孤独，要么庸俗</a>
			</div>
			<div class="describe">愿所有的美好如约而至，愿所有的黑暗都能看到希望，我们都能微笑前行，人生没有完美，有些遗憾才美...</div>
		</div>
	</div>
	<div class="content">
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
		<h1>hahahah</h1>
	</div>
</template>

<script setup lang="ts">
import imgUrl from '../assets/icon/彩色星球.png'

</script>

<style scoped lang="scss">
.index-cover {
	position: absolute;
	width: 100vw;
	height: 100vh;
	top: 0;
	left: 0;
	overflow: hidden;

	.el-image {
		width: 100%;
		height: 100%;
	}

	.triangle {
		position: absolute;
		top: 0;
		left: 0;
		width: 0;
		height: 0;
		border-bottom: 2000px solid rgba(48, 37, 68, 0.73);
		border-right: 1000px solid rgba(36, 145, 191, 0);
		transform: translate(-200px, -1000px);
	}

	.logo-and-menu {
		position: absolute;
		top: 5%;
		width: 100%;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.icon {
			font-size: 30px;
			color: #bc9380;
			border-radius: 5px;
			margin-right: 50px;
			cursor: pointer;
		}
	}

	.cover-title {
		position: absolute;
		top: 50%;
		left: 10%;
		transform: translateY(-50%);
		margin: 0;
		padding: 0;
		font-size: 14px;
		letter-spacing: .4px;
		box-sizing: border-box;
		color: #fff;
		width: 30%;

		.time {
			font-size: 14px;
		}

		.title {
			margin: 15px 0 30px;
			transition: all .3s;

			a {
				font-size: 28px;
				color: #fff;
				cursor: pointer;
				transition: all 0.3s;
				text-decoration: none;
			}
			.title-a:hover {
				text-decoration-line: underline;
				transition: all 0.3s;

			}

		}


		.describe {
			line-height: 22px
		}

	}

}

.content {
	margin-top: 100vh;
}
</style>