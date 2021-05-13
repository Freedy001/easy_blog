<template>
	<div class="root">
		<teleport to="body">
			<div class="title">
				<div class="img">
					<img :src="article.articlePoster" alt="nothing" class="kenburns-top-right">
				</div>
				<div class="content">
					<span class="block">{{ article.title }}</span>
					<span class="author">{{ article.authorName }}</span>
				</div>
				<div class="info">
					<span class="item">时间:2021年 4月 30日</span>
					<span class="item">字数:1023</span>
					<span class="item">喜欢数:1123</span>
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
	</div>
</template>

<script setup lang="ts">
// import 'github-markdown-css'
import {defineComponent, onMounted, onUnmounted, reactive, ref} from "vue";
import {onBeforeRouteLeave, useRoute, useRouter} from "vue-router";
import {get, loadResource} from "../http";
import hljs from 'highlight.js';
import Comment from '../components/Comment.vue'
import CommentList from '../components/CommentList.vue'
import LoadMore from "../components/LoadMore.vue";
import ToTop from "../components/ToTop.vue";
import {ElMessage} from "element-plus";
const route = useRoute();
const router = useRouter();
defineComponent({
	Comment,
	CommentList,
	LoadMore,
	ToTop
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

let article = reactive<IArticle>({})
onMounted(async () => {
	loadStyle()
	loadArticle().then()
	getComments().then()
})
let hasMore=ref(true)
let isShow=ref(false)
//滚动加载评论
let doScroll:any = (srcElement: any) => {
	const scroll: HTMLElement = srcElement.scrollingElement
	if ((scroll.scrollTop + scroll.clientHeight > scroll.scrollHeight - 50)&&hasMore.value) {
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
	setTimeout(()=>{
		const cssLink: HTMLCollectionOf<Element> = document.getElementsByClassName("md-css");
		const length = cssLink.length;
		for (let i = 0; i < length; i++) {
			cssLink[0].remove()
		}
	},300)
	next();
})

interface ICommentItem {
	id: string | number,
	parentName:string|null
	username: string,
	content: string
	creatTime: string,
	child:Array<ICommentItem>
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
			if (data.length==0){
				hasMore.value=false
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
async function commentCB(data:any) {
		commentItem.length = 0
		getComments().then(()=>{
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
.slide-in-bck-top{
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
		}
	}

	.info {
		position: absolute;
		width: 40vw;
		left: 60vw;
		top: 330px;
		height: 30px;
		z-index: 9999;
		display: flex;
		align-items: center;
		justify-content: flex-end;
		color: #ececec;

		.item {
			margin: 10px;
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