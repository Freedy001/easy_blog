<!--suppress CssInvalidPseudoSelector -->
<template>
	<div class="menu" @click="showPop=false">
		<div class="hide-card"></div>
		<div class="search">
			<input v-model="queryString"
			       placeholder="可以是标题、描述、标签、分类,甚至是文章类容"
			       @click.stop=""
			       type="text" class="search-input">
			<transition enter-active-class="slide-in-top1" leave-active-class="slide-out-top1">
				<div class="pop" v-if="showPop">
					<div class="search-item" v-for="item in suggest">
						<div class="value">
							<div class="content">
								<img :src="item.logo" style="width: 25px;height: 25px;object-fit: cover" alt="">
								<div v-html="item.content"></div>
							</div>
							<span>来自:<b style="font-size: 12px">{{ item.title }}</b></span>
						</div>
						<div class="jump">
							<span>jump to</span>
							<img :src="enter" alt="">
						</div>
					</div>
				</div>
			</transition>
		</div>
		<div class="menu-item">
			<div class="index item" @click="$router.push('/');$emit('clickCb')">index</div>
			<div class="shorthand item">shorthand</div>
			<div class="subscribe item">subscribe</div>
			<div class="about item">about</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import {defineProps, reactive, ref, watch} from "vue";
import articleCategory from '../assets/icon/articleCategory.svg'
import articleDesc from '../assets/icon/articleDesc.svg'
import articleTags from '../assets/icon/articleTags.svg'
import content from '../assets/icon/content.svg'
import title from '../assets/icon/title.svg'
import enter from '../assets/icon/enter.svg'
import {get} from "../http";

defineProps(['clickCb'])

interface ISuggest {
	field: string
	content: string
	title: string
	logo:any
}

let search = ref()
let showPop = ref(false)
let queryString = ref()
let suggest = reactive<Array<ISuggest>>([])
let timeout;
watch(queryString, (val) => {
	if (timeout) {
		clearTimeout(timeout);
		timeout = null;
	}
	timeout = setTimeout(async () => {
		if (queryString.value !== '') {
			const response = await get(`/search/getSuggestions?queryString=${val}`);
			if (response.code == 200) {
				const resSuggest: Array<ISuggest> = response.data
				if (resSuggest.length != 0) {
					suggest.length = 0
					resSuggest.forEach((value, index) => {
						if (value.field=='articleCategory'){
							value.logo=articleCategory;
						}else if (value.field=='articleDesc'){
							value.logo=articleDesc;
						}else if (value.field=='articleTags'){
							value.logo=articleTags;
						}else if (value.field=='content'){
							value.logo=content;
						}else if (value.field=='title'){
							value.logo=title;
						}
						suggest.push(value)
					})
					showPop.value = true
				}
			}
		}
	}, 500);
})


</script>

<style scoped lang="scss">
.menu {
	position: fixed;
	z-index: 800;
	background-color: rgba(255, 255, 255, 0.96);
	width: 100vw;
	height: 100vh;
	top: 0;

	.hide-card {
		position: absolute;
		width: 100%;
		height: 30vh;
		display: none;
		z-index: 2000;
	}

	.search {
		top: 30%;
		width: 100%;
		position: absolute;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		z-index: 1000;

		.search-input {
			width: 800px;
			height: 48px;
			outline: none;
			border: 1px solid #dedede;
			background-color: white;
			font-size: inherit;
			color: #898989;
			border-radius: 5px;
			transition: all 0.1s ease;
			text-indent: 20px;
			z-index: 9999999;

			&:focus {
				border: 1px solid #83cd5d;
			}

			&::-webkit-input-placeholder {
				color: #a1a1a1;
				font-size: inherit;
				text-indent: 20px
			}
		}

		.pop {
			width: 800px;
			height: 500px;
			background-color: rgb(255, 255, 255);
			border-radius: 5px;
			overflow: auto;

			.search-item {
				margin: 6px;
				padding: 5px;
				border-radius: 5px;
				//height: 60px;
				transition: all .3s ease;
				display: flex;
				justify-content: space-between;
				align-items: center;
				cursor: pointer;

				.value {
					display: flex;
					align-items: center;
					margin-left: 5px;
					justify-content: space-between;
					width: 670px;
					line-height: 28px;
					font-size: 13px;
					.content{
						display: flex;
						width: 500px;
						align-items: center;
						img {
							margin-right: 10px;
						}
					}
					span{
						width: 150px;
					}
				}

				.jump {
					position: relative;
					border: 1px solid #c6c6c6;
					padding: 0 12px 0 12px;
					height: 25px;
					align-items: center;
					border-radius: 5px;
					background-color: white;
					//display: none;
					display: flex;
					margin-right: 10px;

					span {
						margin: 5px;
						font-size: 10px;
						font-weight: lighter;
						color: #535353;
					}

					img {
						position: absolute;
						bottom: 3px;
						right: 3px;
						width: 8px;
						height: 12px;
						color: #797979;
					}
				}

				&:hover {
					background-color: #0366d6;

					.jump {
						display: flex;
						cursor: pointer;
					}
				}
			}
		}

		:deep(.el-autocomplete) {
			width: 800px;
			height: 50px;
		}

		:deep(.el-input) {
			width: 100%;
			height: 100%;
		}

		:deep(.el-input__inner) {
			width: 100%;
			height: 100%;
		}
	}

	.menu-item {
		top: 45%;
		position: relative;
		display: flex;
		justify-content: center;
		align-items: center;

		.item {
			margin: 30px;
			font-size: 25px;
			font-weight: lighter;
			cursor: pointer;
			color: #5c5a5a;
			font-family: "Helvetica Neue", serif;
		}
	}
}

:deep(.popper) {
	height: 500px;
}


:deep(.popper .el-autocomplete-suggestion__wrap) {
	max-height: 500px;
}

.slide-in-top1 {
	animation: slide-in-top1 0.3s both;
}

@-webkit-keyframes slide-in-top1 {
	0% {
		height: 0;
	}
}

.slide-out-top1 {
	animation: slide-out-top1 0.3s both;
}

@keyframes slide-out-top1 {
	100% {
		height: 0;
	}
}

</style>