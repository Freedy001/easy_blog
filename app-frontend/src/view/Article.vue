<!--suppress JSUnresolvedVariable -->
<template>
	<div class="root">
		<teleport to="body">
			<div class="title">
				<div class="img">
					<img :src="loadResource(article.articlePoster)" alt="nothing" class="kenburns-top-right">
				</div>
				<div class="content">
					<span class="block">{{ article.title }}</span>
					<span class="author" @click="handleUserInfo(article.authorName,$event)">{{ article.authorName }}</span>
				</div>
				<div class="info">
					<span class="item">时间: {{ article.publishTime == null ? 0 : article.publishTime }}</span>
					<span class="item">字数:{{ article.wordNum == null ? 0 : article.wordNum }}</span>
					<span class="item">喜欢数:{{ article.likeNum == null ? 0 : article.likeNum }}</span>
					<div :style="likeStyle" class="like" :class="{'bounce-top':showWave,'click':clickClass}">
						<div class="before" :style="likeStyle"></div>
						<div class="after" :style="likeStyle"></div>
					</div>
					<div :style="likeStyle" @click="like" @mouseenter="enterLike" @mouseleave="leaveLike" class="like">
						<div class="before" :style="likeStyle"></div>
						<div class="after" :style="likeStyle"></div>
					</div>
				</div>
			</div>
		</teleport>
		<article class="markdown-body" v-html="article.content">
		</article>
		<Comment :id="$route.query.id" @commentCB="commentCB"></Comment>
		<div class="comment-header">
			<span id="CommentList">Comment List</span>
			<span>({{ article.commentNum }})</span>
		</div>
		<transition-group enter-active-class="slide-in-bck-top">
			<CommentList class="comment-list" @commentCB="commentCB" :commentItem="item" :key="item.id"
			             v-for="item in commentItem"></CommentList>
		</transition-group>
		<div>
			<LoadMore v-if="isShow" :hasMore="hasMore"></LoadMore>
		</div>
		<ToTop @scroll="doScroll"></ToTop>
		<UserInfo :startX="userInfo.x" :stratY="userInfo.y" :nickname="userInfo.nickname"></UserInfo>
	</div>
</template>

<script setup lang="ts">
// import 'github-markdown-css'
import {defineComponent, onMounted, onUnmounted, reactive, ref, watch} from "vue";
import {onBeforeRouteLeave, useRoute, useRouter} from "vue-router";
import {get, loadResource} from "../http";
import hljs from 'highlight.js';
import Comment from '../components/Comment.vue'
import CommentList from '../components/CommentList.vue'
import UserInfo from '../components/UserInfo.vue'
import LoadMore from "../components/LoadMore.vue";
import ToTop from "../components/ToTop.vue";
// import 'highlight.js/styles/androidstudio.css'
import {ElMessage} from "element-plus";
const route = useRoute();
const router = useRouter();
defineComponent({
	Comment,
	CommentList,
	LoadMore,
	ToTop,
	UserInfo
})
watch(() => route.query.id, () => {
	loadArticle()
	page = 1
	getComments()
})
onMounted(()=>{
	let AutocJs = require('autocjs');

// 创建 Outline 实例
	let navigation = new AutocJs({
		// 文章正文 DOM 节点的 ID 选择器
		article: '#article',
		// 要收集的标题选择器
		selector: 'h1,h2,h3,h4,h5,h6',
		// 侧边栏导航的标题
		title: '文章导读',
		// 文章导读导航的位置
		// outside - 以侧边栏菜单形式显示（默认值）
		// inside - 在文章正文一开始的地方显示
		position: 'outside',
		// 标题图标链接的 URL 地址
		// （默认）没有设置定制，点击链接页面滚动到标题位置
		// 设置了链接地址，则不会滚动定位
		anchorURL: '',
		// 链接的显示位置
		// front - 在标题最前面（默认值）
		// back - 在标题后面
		anchorAt: 'front',
		// 是否生成文章导读导航
		isGenerateOutline: true,
		// 是否在文章导读导航中显示段落章节编号
		isGenerateOutlineChapterCode: true,
		// 是否在正文的文章标题中显示段落章节编号
		isGenerateHeadingChapterCode: false,
		// 是否在正文的文章标题中创建锚点
		isGenerateHeadingAnchor: true
	});

// 可以在创建导航后，重置配置信息，重新生成新的导航
	navigation.reload({
		// 调整位直接在文章内生成导航
		position: 'outside',
		// 并且在文章标题前显示段落的章节层次索引值
		isGenerateHeadingChapterCode: true
	})

})





let userInfo=reactive<any>({})
function handleUserInfo(name:any,event:any){
	userInfo.nickname=name;
	userInfo.x=event.clientX
	userInfo.y=event.clientY
}

//以下是喜欢样式
let likeStyle = reactive({
	"background-color": "rgb(205,205,205)"
})
//红心波浪
let showWave = ref(false)
//点赞时触发
let clickClass = ref(false)
//点赞时触发 禁止鼠标移开导致的样式改变
let enableLeave = true
function enterLike() {
	showWave.value = true
	likeStyle["background-color"] = "tomato"
}
function leaveLike() {
	if (enableLeave) {
		showWave.value = false
		likeStyle["background-color"] = "rgb(205,205,205)"
	}
}
let timeout:any;
async function like() {
	if (timeout){
		clearTimeout(timeout);
		clickClass.value = false
	}
	if (route.query.id) {
		const promise = await get(`/article/likeArticle?id=${route.query.id}`);
		if (promise.code == 200) {
			article.likeNum++;
			localStorage.setItem(route.query.id + "", "like");
			enableLeave = false
			likeStyle["background-color"] = "tomato"
			clickClass.value = true
			showWave.value = true
			timeout = setTimeout(() => {
				clickClass.value = false
			}, 2000);
		}
	}
}
onMounted(()=>{
	if (localStorage.getItem(route.query.id + "")){
		enableLeave = false
		likeStyle["background-color"] = "tomato"
		showWave.value = true
	}
})


interface IArticle {
	id: number | string,
	title: string,
	content: string,
	publishTime: string,
	articlePoster: string,
	wordNum: number,
	authorName: number,
	commentNum: number,
	"likeNum": string,
}

let article = reactive<IArticle|any>({})
onMounted(async () => {
	loadStyle()
	loadArticle().then()
	getComments().then()
})
let hasMore = ref(true)
let isShow = ref(false)
//滚动加载评论
let doScroll: any = (srcElement: any) => {
	const scroll: HTMLElement = srcElement.scrollingElement
	if ((scroll.scrollTop + scroll.clientHeight > scroll.scrollHeight - 50) && hasMore.value) {
		isShow.value = true
		page++;
		getComments().then(() => {
			isShow.value = !hasMore.value;
		})
	}
}

/**
 * 加载样式
 */
function loadStyle() {
	const head = document.getElementsByTagName('head')[0];
	const mdLink = document.createElement('link');
	const hjLink = document.createElement('link');
	mdLink.href = loadResource('/css/md.css')
	mdLink.setAttribute("rel", "stylesheet")
	mdLink.setAttribute("class", "md-css")
	hjLink.href = loadResource('/css/hj.css')
	hjLink.setAttribute("rel", "stylesheet")
	hjLink.setAttribute("class", "md-css")
	head.appendChild(mdLink);
	head.appendChild(hjLink);
}

/**
 * 文章
 */
async function loadArticle() {
	let id = route.query.id;
	if (id) {
		const response = await get(`/article/get?id=${id}`);
		if (response.code === 200) {
			const data: IArticle = response.data
			article.id = data.id
			article.title = data.title
			article.content = data.content
			article.publishTime = data.publishTime
			article.articlePoster = data.articlePoster
			article.wordNum = data.wordNum
			article.likeNum = data.likeNum
			article.commentNum = data.commentNum
			article.authorName = data.authorName
			setTimeout(() => {
				hljs.highlightAll()
			}, 100)
		}
	}
}

/**
 * 清除样式 防止干扰其他页面
 */
onBeforeRouteLeave((to, from, next) => {
	setTimeout(() => {
		const cssLink: HTMLCollectionOf<Element> = document.getElementsByClassName("md-css");
		const length = cssLink.length;
		for (let i = 0; i < length; i++) {
			cssLink[0].remove()
		}
	}, 300)
	next();
})

interface ICommentItem {
	id: string | number,
	parentName: string | null
	username: string,
	content: string
	creatTime: string,
	child: Array<ICommentItem>
}

let commentItem = reactive<Array<ICommentItem>>([])

let page = 1;

/**
 * 加载评论
 */
async function getComments() {
	const id = route.query.id;
	if (id) {
		const response = await get(`/comment/getList?articleId=${id}&page=${page}&limit=10`);
		if (response.code == 200) {
			const data: Array<ICommentItem> = response.data.list
			if (data.length == 0) {
				hasMore.value = false
			}
			data.forEach((value, index) => {
				commentItem.push(value)
			})
		}
	}
}

/**
 * 评论成功的回调
 */
async function commentCB(data: any) {
	commentItem.length = 0
	getComments().then(() => {
		const element: any = document.getElementById("CommentList")
		window.scrollTo({
			top: element.offsetTop,
			behavior: "smooth"
		});
		ElMessage({
			showClose: true,
			message: '评论发布成功😎😎!'
		});
	})
}
</script>

<style scoped lang="scss">
.kenburns-top-right {
	animation: kenburns-top-right 5s ease-out both;
}

@keyframes kenburns-top-right {
	0% {
		transform: scale(1) translate(0, 0);
		transform-origin: 84% 16%;
	}
	100% {
		transform: scale(1.25) translate(20px, -15px);
		transform-origin: right top;
	}
}

.slide-in-bck-top {
	animation: slide-in-bck-top 0.6s cubic-bezier(0.250, 0.460, 0.450, 0.940) both;
}

@keyframes slide-in-bck-top {
	0% {
		transform: translateZ(700px) translateY(-300px);
		opacity: 0;
	}
	100% {
		transform: translateZ(0) translateY(0);
		opacity: 1;
	}
}

.bounce-top {
	animation: ping 1.5s ease-in-out infinite both;
}

@keyframes ping {
	0% {
		transform: scale(0.2) rotate(-45deg);
		opacity: 0.8;
	}
	80% {
		transform: scale(2.2) rotate(-45deg);
		opacity: 0;
	}
	100% {
		transform: scale(3.2) rotate(-45deg);
		opacity: 0;
	}
}

.click {
	cursor: none;
	animation: clickLike 1.5s linear both;
}

@keyframes clickLike {
	0% {
		transform: scale(0.5) rotate(-45deg);
		opacity: 1;
	}
	25% {
		transform: translate(-50vw, 100px) scale(1) rotate(-45deg);
		opacity: 1;
	}
	75% {
		transform: translate(-50vw, 100px) scale(200) rotate(-45deg);
		opacity: 0.5;
	}
	100% {
		transform: translate(-50vw, 100px) scale(300) rotate(-45deg);
		opacity: 0;
	}
}

::v-global(.markdown-body .hljs) {
	color: black;
}

.markdown-body {
	overflow: hidden;
	padding-top: 400px;
}

.title {
	width: 100%;
	height: 360px;
	background-color: #5d6874;
	background-position-y: 50%;
	position: absolute;
	top: 0;
	left: 0;

	.img {
		position: absolute;
		width: 100%;
		height: 100%;
		overflow: hidden;

		img {
			width: 100%;
			height: 100%;
			object-fit: cover;
			filter: blur(3px);
			border-radius: 5px;

		}
	}

	.content {
		border-radius: 5px;
		position: absolute;
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;

		.block {
			filter: blur(0.1px);
			font-size: 50px;
			color: #ececec;
			font-family: "Open Sans", sans-serif;
			font-weight: 700;
		}

		.author {
			font-size: 25px;
			color: #ececec;
			font-family: "Open Sans", sans-serif;
			font-weight: 700;
			margin-top: 50px;
			&:hover{
				cursor: pointer;
				text-decoration-line: underline;
				text-decoration-color: #3a9ff5;
				color: #0b9aff;
			}
		}
	}

	.info {
		position: absolute;
		width: 40vw;
		right: 30px;
		top: 325px;
		height: 30px;
		z-index: 500;
		display: flex;
		align-items: center;
		justify-content: flex-end;
		color: #ececec;

		.item {
			margin: 10px;
		}

		.like {
			position: absolute;
			margin-left: 5px;
			margin-top: 3px;
			right: -15px;
			width: 12px;
			height: 12px;
			transform: rotate(-45deg);
			cursor: pointer;
			transition: all .3s ease;
		}

		.before {
			content: "";
			position: absolute;
			top: -6px;
			left: 0;
			width: 12px;
			height: 12px;
			border-radius: 50%;
			transition: all .3s ease;
		}

		.after {
			content: "";
			position: absolute;
			top: 0;
			left: 6px;
			width: 12px;
			height: 12px;
			border-radius: 50%;
			transition: all .3s ease;
		}
	}

}

.comment-header {
	margin: 50px auto;
	width: 850px;
	font-style: oblique;
	display: block;
	font-size: 20px;
	text-decoration: none;
}


</style>