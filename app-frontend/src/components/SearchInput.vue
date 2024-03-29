<template>
	<div class="search">
		<div class="logo" v-if="showLogo">
			<img style="transition: all 1s ease" :src="isDarkMode()?easySearchDark:easySearch" alt="">
		</div>
		<div class="search-area" :class="addDarkClass()">
			<div class="input-pop">
				<input v-model="queryString"
				       placeholder="可以是标题、描述、标签、分类,甚至是文章内容"
				       @keypress.enter="$router.push(`/search?searchString=${queryString}`);$emit('searchCb')"
				       id="searchInput"
				       autocomplete="off"
				       @focus="changeStyle"
				       @focusout="outChangeStyle"
				       @click.stop=""
				       :style="focusStyle"
				       type="text" class="search-input">
				<transition enter-active-class="slide-in-top1" leave-active-class="slide-out-top1">
					<div class="pop" id="pop" v-if="showPop">
						<div class="search-item" v-for="item in suggest"
						     @click="$router.push(`/search?searchString=${item.title}`);$emit('searchCb')">
							<div class="value">
								<div class="content">
									<el-tooltip placement="left" :content="item.logoExplain" :effect="isDarkMode()?'dark':'light'">
										<img :src="item.logo" style="width: 25px;height: 25px;object-fit: cover" alt="">
									</el-tooltip>
									<div v-html="item.content"></div>
								</div>
								<span>来自:<b style="font-size: 12px">{{ item.title }}</b></span>
							</div>
							<div class="jump" @click.stop="$router.push(`/article?id=${item.id}`);$emit('searchCb')">
								<span>jump to</span>
								<img :src="enter" alt="">
							</div>
						</div>
					</div>
				</transition>
			</div>
			<div class="searchButton" @click="$router.push(`/search?searchString=${queryString}`);$emit('searchCb')">搜索</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import {defineEmit, defineProps, getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {get} from "../http";
import easySearch from "../assets/icon/easySearch.svg";
import easySearchDark from "../assets/icon/easySearch-dark.svg";
import articleCategory from "../assets/icon/articleCategory.svg";
import articleCategoryDark from "../assets/icon/articleCategory-dark.svg";
import articleDesc from "../assets/icon/articleDesc.svg";
import articleDescDark from "../assets/icon/articleDesc-dark.svg";
import articleTags from "../assets/icon/articleTags.svg";
import articleTagsDark from "../assets/icon/articleTags-dark.svg";
import content from "../assets/icon/content.svg";
import contentDark from "../assets/icon/content-dark.svg";
import title from "../assets/icon/title.svg";
import titleDark from "../assets/icon/title-dark.svg";
import enter from "../assets/icon/enter.svg";
import {loadResource} from "../http";
import {useRoute} from "vue-router";
import {addDarkClass, isDarkMode} from "../utils/common";

defineProps(['showLogo'])
defineEmit(['searchCb'])
const route = useRoute();

interface ISuggest {
	field: string
	content: string
	title: string
	logo: any
	logoExplain: string
}

//以下是获取建议
let search = ref()
let showPop = ref(false)
let queryString = ref<string|any>('')
let suggest = reactive<Array<any>>([])
let timeout: number | null | undefined;
watch(queryString, (val) => {
	if (timeout) {
		//防抖
		clearTimeout(timeout);
		timeout = null;
	}
	timeout = setTimeout(async () => {
		showPop.value = false
		if (queryString.value !== '' && queryString.value !== route.query.searchString) {
			const response = await get(`/search/getSuggestions?queryString=${val}`);
			if (response.code == 200) {
				const resSuggest: Array<ISuggest> = response.data
				if (resSuggest.length != 0) {
					suggest.length = 0
					resSuggest.forEach((value, index) => {
						if (value.field == 'articleCategory') {
							value.logo = isDarkMode()?articleCategoryDark:articleCategory;
							value.logoExplain = '分类';
						} else if (value.field == 'articleDesc') {
							value.logo = isDarkMode()?articleDescDark:articleDesc;
							value.logoExplain = '描述';
						} else if (value.field == 'articleTags') {
							value.logo = isDarkMode()?articleTagsDark:articleTags;
							value.logoExplain = '标签';
						} else if (value.field == 'content') {
							value.logo = isDarkMode()?contentDark:content;
							value.logoExplain = '内容';
						} else if (value.field == 'title') {
							value.logo = isDarkMode()?titleDark:title;
							value.logoExplain = '标题';
						}
						suggest.push(value)
					})
					showPop.value = true
				}
			}
		}
	}, 500);
})

//以下都是修改样式
let focusStyle = reactive<any>({})

function changeStyle() {
	if (!showPop.value) {
		focusStyle['border-top'] = "2px solid #4e71f2";
		focusStyle['border-left'] = "2px solid #4e71f2";
		focusStyle['border-right'] = "2px solid #4e71f2";
		focusStyle['border-bottom'] = "2px solid #4e71f2";
	}
}

watch(showPop, (val) => {
	if (val) {
		focusStyle['border-bottom'] = "none";
		focusStyle['border-bottom-left-radius'] = "0";
	} else {
		outChangeStyle()
		focusStyle['border-bottom-left-radius'] = "10px";
	}
})

function outChangeStyle() {
	if (!showPop.value) {//454545
		focusStyle['border-top'] = `2px solid ${isDarkMode()?'#454545':'#dedede'}`;
		focusStyle['border-left'] =`2px solid ${isDarkMode()?'#454545':'#dedede'}`;
		focusStyle['border-right'] = `2px solid ${isDarkMode()?'#454545':'#dedede'}`;
		focusStyle['border-bottom'] = `2px solid ${isDarkMode()?'#454545':'#dedede'}`;
	}
}

onMounted(() => {
	document.body.onclick = () => {
		showPop.value = false
	}
	//同步搜索
	if (route.query.searchString) {
		queryString.value = route.query.searchString
	}
})
//同步搜索
watch(() => route.query.searchString, () => {
	queryString.value = route.query.searchString
})
</script>

<style scoped lang="scss">
.search {
	width: 100%;
	position: absolute;
	display: flex;
	justify-content: center;
	align-items: center;
	flex-direction: column;
	z-index: 1000;

	.logo {
		img {
			width: 400px;
			height: 100px;
			object-fit: cover;
		}
	}

	.search-area {
		display: flex;
		.input-pop {
			position: relative;

			.search-input {
				width: 800px;
				height: 48px;
				outline: none;
				border: 1px solid #dedede;
				background-color: white;
				font-size: inherit;
				color: #898989;
				transition: all 0.3s ease;
				text-indent: 20px;
				z-index: 9999999;
				border-top-left-radius: 10px;
				border-bottom-left-radius: 10px;

				&::-webkit-input-placeholder {
					color: #a1a1a1;
					font-size: inherit;
					text-indent: 20px
				}
			}

			.pop {
				position: absolute;
				width: 800px;
				height: 500px;
				background-color: rgb(255, 255, 255);
				border-left: 2px solid #4e71f2;
				border-right: 2px solid #4e71f2;
				border-bottom: 2px solid #4e71f2;
				border-bottom-left-radius: 10px;
				border-bottom-right-radius: 10px;
				overflow: auto;
				overscroll-behavior: contain;

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

						.content {
							display: flex;
							width: 500px;
							align-items: center;

							img {
								margin-right: 10px;
							}
						}

						span {
							width: 150px;
						}
					}

					.jump {
						position: relative;
						border: 1px solid #c6c6c6;
						padding: 0 5px 0 5px;
						height: 20px;
						align-items: center;
						border-radius: 5px;
						background-color: white;
						display: none;
						margin-right: 10px;
						transition: all .3s ease;

						span {
							margin: 5px;
							font-size: 10px;
							font-weight: lighter;
							color: #535353;
						}

						img {
							position: absolute;
							bottom: -2px;
							right: 1px;
							width: 8px;
							height: 12px;
							color: #797979;
						}

						&:hover {
							transform: scale(1.05);
							span {
								color: #000000;
							}
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
		}

		.searchButton {
			height: 52px;
			background-color: #4e6ef2;
			border-top-right-radius: 10px;
			border-bottom-right-radius: 10px;
			text-align: center;
			line-height: 49px;
			color: white;
			font-size: 20px;
			width: 100px;
			cursor: pointer;
			user-select: none;
			transition: all .1s ease;

			&:hover {
				background-color: #4662d9;
			}
		}
	}

	.search-area.dark{

		.input-pop {
			.search-input {
				color: #d9d9d9;
				border: 2px solid #454545;
				background-color: #0d1117;

				&::-webkit-input-placeholder {
					color: #a1a1a1;
				}
			}

			.pop {
				background-color: #0d1117;
				border-left: 2px solid #4e71f2;
				border-right: 2px solid #4e71f2;
				border-bottom: 2px solid #4e71f2;
				.search-item {
					.jump {
						border: 2px solid #454545;
						background-color: #0d1117;
						span {
							color: #d9d9d9;
						}

						&:hover {
							span {
								color: #ffffff;
							}
						}
					}

				}

			}
		}

		.searchButton{
			background-color: #273753;
			color: white;
			&:hover {
				background-color: #4662d9;
			}
		}
	}


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