<!--suppress JSUnresolvedVariable -->
<template>
<div class="root">
	<teleport to="body">
		<div id="cover" class="index-cover" ref="container">
			<div class="img" :style="moveStyle">
				<img :src="loadResource($store.state.indexSetting.poster)" alt="">
			</div>
			<div class="triangle" :style="{'border-bottom': `400vh solid ${$store.state.indexSetting.indexColor}`}"></div>
			<div class="cover-title">
				<div class="time">{{ $store.state.indexSetting.indexArticle.publishTime }}</div>
				<div class="title">
					<router-link :to="`/article?id=${$store.state.indexSetting.indexArticle.id}`" tag="a" data-v-241ea8f0="" class="title-a">{{$store.state.indexSetting.indexArticle.title}}</router-link>
				</div>
				<div class="describe">{{$store.state.indexSetting.indexArticle.articleDesc}}</div>
			</div>
		</div>
	</teleport>
	<div class="content">
		<div :class="darkModeClass('line')"></div>
		<transition-group
				enter-active-class="fade-in-right"
				leave-active-class="fade-out-left"
				mode="out-in"
		>
			<SimpleArticleContainer
					v-for="(item,i) in articleItem"
					:articleItem="item"
					:key="i"
					:left="i%2===0">
			</SimpleArticleContainer>
		</transition-group>
		<div>
			<LoadMore v-if="isShow" :hasMore="hasMore"></LoadMore>
		</div>
		<ToTop @scroll="doScroll"></ToTop>
	</div>
</div>
</template>

<script setup lang="ts">
import {defineComponent, onMounted, reactive, ref, watch} from "vue";
import SimpleArticleContainer from '../components/SimpleArticleContainer.vue'
import LoadMore from "../components/LoadMore.vue";
import {get, loadResource} from "../http";
import ToTop from "../components/ToTop.vue";
import {useStore} from "vuex";
import {darkModeClass} from "../utils/common";
const store = useStore();

defineComponent({
	SimpleArticleContainer,
	LoadMore,
	ToTop
})
watch(()=>store.state.notifyReloadArticle,()=>{
	articleItem.length=0
	isShow.value=true;
	page=1;
	getArticleList().then(()=>{
		isShow.value = !hasMore.value;
	})
})

let moveStyle = reactive<{ transform: string, transition: string | null }>({
	transform: 'translate(0,-10vh)',
	transition: null,
});
onMounted(() => {
	initIndexMove()
	getArticleList()
})
let hasMore=ref(true)
let isShow = ref(false)
let doScroll:any = (srcElement: any) => {
	const scroll: HTMLElement = srcElement.scrollingElement
	if ((scroll.scrollTop + scroll.clientHeight > scroll.scrollHeight - 50)&&hasMore.value) {
		isShow.value=true;
		page++;
		getArticleList().then(()=>{
			isShow.value = !hasMore.value;
		})
	}
}

/**
 * 初始首页动画
 */
function initIndexMove() {
	let cover: any = document.getElementById("cover");
	//绑定移动动画
	cover.onmousemove = (event: any) => {
		moveStyle.transition = null;
		const vw = cover.clientWidth
		const vh = cover.clientHeight
		const middleX = vw / 2
		const middleY = vh / 2
		const x = event.pageX
		const y = event.pageY
		const offsetX = x - middleX
		const offsetY = middleY - y
		moveStyle.transform = `translate(${-0.05 * vw - (offsetX / middleX) * 0.05 * vw}px,
	${-0.05 * vh + (offsetY / middleY) * 0.05 * vh}px)`
	}
	cover.onmouseleave = () => {
		moveStyle.transition = 'all 0.5s';
		moveStyle.transform = 'translate(-5vw,-5vh)';
		//移除屏幕时关掉鼠标移动动画
	}
	setTimeout(()=>{
		moveStyle.transition = 'all 0.5s';
		moveStyle.transform = 'translate(-5vw,-5vh)';
	},500)
}

interface IArticleItem {
	id:string
	articlePoster: string,
	publishTime: string,
	title: string,
	articleDesc: string,
	authorName: string,
	articleCategory: string,
	articleTags: Array<string>,
	wordNum: number,
	visitNum: number,
	likeNum: number
}

let articleItem = reactive<Array<IArticleItem>>([])
let page = 1
/**
 * 加载数据
 */
async function getArticleList() {
	const response = await get(`/article/list?page=${page}&limit=5`);
	if (response.code == 200) {
		const data: Array<IArticleItem> = response.data.list
		if (data.length==0) {
			hasMore.value=false;
		} else {
			data.forEach((value, index) => {
				articleItem.push({
					id:value.id,
					articlePoster: value.articlePoster,
					publishTime: value.publishTime,
					title: value.title,
					authorName: value.authorName,
					articleDesc: value.articleDesc,
					articleCategory: value.articleCategory,
					articleTags: value.articleTags,
					wordNum: value.wordNum,
					visitNum: value.visitNum,
					likeNum: value.likeNum,
				})
			})
		}
	}
}
</script>

<style scoped lang="scss">
.index-cover {
	position: absolute;
	width: 100vw;
	height: 100vh;
	top: 0;
	left: 0;
	overflow: hidden;

	.img {
		width: 110vw;
		height: 110vh;
		img {
			width: 100%;
			height: 100%;
			object-fit: cover;
		}
	}

	.triangle {
		position: absolute;
		top: 0;
		left: 0;
		width: 0;
		height: 0;
		border-bottom: 400vh solid rgba(45, 37, 73, 0.7);
		border-right: 130vw solid rgba(36, 145, 191, 0);
		transform: translate(-25vw, -150vh);
		transition: all 0.3s;
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
	position: relative;
	top:  calc(100vh);
	max-width: 1200px;
	margin-left: auto;
	margin-right: auto;
	min-height: 1000px;

	.line {
		content: "";
		width: 1px;
		height: calc(100% + 100px);
		position: absolute;
		left: 50%;
		background: #eaeaea;
		z-index: -99;
	}

	.line-dark {
		content: "";
		width: 1px;
		height: calc(100% + 100px);
		position: absolute;
		left: 50%;
		background: #2d2d2d;
	}
}


</style>