<!--suppress ALL -->
<template>
  <div class="root">
    <teleport to="body">
      <div class="title">
        <div class="img">
          <img :src="loadHdResource(article.articlePoster)" alt="nothing" class="kenburns-top-right">
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
    <div class="article-container" :class="{'to-center':!showToc,'to-right':showToc}">
      <article class="markdown-body" id="markdown" v-html="article.content">
      </article>
    </div>
    <transition enter-active-class="slide-in-left" leave-active-class="slide-out-left">
      <div class="table-of-content" :class="addDarkClass()" v-show="showToc" id="toc"></div>
    </transition>
    <div v-if="article.articleComment==0">
      <Comment :id="commentId" class="comment-component" @commentCB="commentCB"></Comment>
      <div class="comment-header" v-if="commentNum!=0">
        <span id="CommentList">Comment List</span>
        <span>({{ commentNum }})</span>
      </div>
      <div v-else class="no-comment">呜呜呜😭~没有评论,你赶紧评论一个吧！</div>
      <transition-group enter-active-class="slide-in-bck-top">
        <CommentList class="comment-list" @commentCB="commentCB" :commentItem="item" :key="item.id"
                     v-for="item in commentItem"></CommentList>
      </transition-group>
      <div>
        <LoadMore v-if="isShow" :hasMore="hasMore"></LoadMore>
      </div>
    </div>
    <div class="no-comment" v-else>
      博主关闭了该文章的评论功能
    </div>
    <UserInfo :startX="userInfo.x" :stratY="userInfo.y" :nickname="userInfo.nickname"></UserInfo>
  </div>
</template>

<script setup lang="ts">
import {defineComponent, onMounted, onUnmounted, reactive, ref, watch} from "vue";
import {onBeforeRouteLeave, useRoute, useRouter} from "vue-router";
import {get, loadHdResource, loadResource} from "../http";
import hljs from 'highlight.js';
import Comment from '../components/Comment.vue'
import CommentList from '../components/CommentList.vue'
import UserInfo from '../components/UserInfo.vue'
import LoadMore from "../components/LoadMore.vue";
import FullScreenLoading from "../components/FullScreenChanging.vue";
import {ElMessage} from "element-plus";
import {addDarkClass, copyProperties, isDarkMode} from "../utils/common";
import {useStore} from "vuex";

const route = useRoute();
const router = useRouter();
let commentId: any = route.query.id
defineComponent({
    Comment,
    CommentList,
    LoadMore,
    UserInfo,
    FullScreenLoading
})
watch(() => route.query.id, () => {
    loadArticle()
    page = 1
    getComments()
})
let isOk = ref()
//用户介绍
let userInfo = reactive<any>({})

function handleUserInfo(name: any, event: any) {
    userInfo.nickname = name;
    userInfo.x = event.clientX
    userInfo.y = event.clientY
}

//以下是喜欢样式
let likeStyle = reactive({
    "backgroundColor": "rgb(205,205,205)"
})
//红心波浪
let showWave = ref(false)
//点赞时触发
let clickClass = ref(false)
//点赞时触发 禁止鼠标移开导致的样式改变
let enableLeave = true

function enterLike() {
    showWave.value = true
    likeStyle["backgroundColor"] = "tomato"
}

function leaveLike() {
    if (enableLeave) {
        showWave.value = false
        likeStyle["backgroundColor"] = "rgb(205,205,205)"
    }
}

let timeout: any;

async function like() {
    if (timeout) {
        clearTimeout(timeout);
        clickClass.value = false
    }
    if (route.query.id) {
        const promise = await get(`/article/likeArticle?id=${route.query.id}`);
        if (promise.code == 200) {
            article.likeNum++;
            localStorage.setItem(route.query.id + "", "like");
            enableLeave = false
            likeStyle["backgroundColor"] = "tomato"
            clickClass.value = true
            showWave.value = true
            timeout = setTimeout(() => {
                clickClass.value = false
            }, 2000);
        }
    }
}

onMounted(() => {
    if (localStorage.getItem(route.query.id + "")) {
        enableLeave = false
        likeStyle["backgroundColor"] = "tomato"
        showWave.value = true
    }
})

//以下是加载文章
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

let article = reactive<IArticle | any>({})
onMounted(async () => {
    loadStyle()
    loadArticle().then()
    getComments().then()
})

/**
 * 加载样式
 */
function loadStyle() {
    const head = document.getElementsByTagName('head')[0];
    const mdLink = document.createElement('link');
    const hjLink = document.createElement('link');
    mdLink.href = loadResource(isDarkMode() ? '/resource/md-dark.css' : '/resource/md.css')
    mdLink.setAttribute("rel", "stylesheet")
    mdLink.setAttribute("class", "md-css")
    hjLink.href = loadResource(isDarkMode() ? '/resource/hj-dark.css' : '/resource/hj.css')
    hjLink.setAttribute("rel", "stylesheet")
    hjLink.setAttribute("class", "md-css")
    head.appendChild(mdLink);
    head.appendChild(hjLink);
}

const store = useStore();
watch(() => store.state.darkMode, () => {
    clearStyle()
    loadStyle()
})

/**
 * 清除样式 防止干扰其他页面
 */
function clearStyle() {
    const cssLink: HTMLCollectionOf<Element> = document.getElementsByClassName("md-css");
    const length = cssLink.length;
    for (let i = 0; i < length; i++) {
        cssLink[0].remove()
    }
}

onBeforeRouteLeave((to, from, next) => {
    setTimeout(() => {
        clearStyle()
    }, 300)
    next();
})

/**
 * 文章
 */
async function loadArticle() {
    let id = route.query.id;
    let name = route.query.articleName;
    if (id) {
        const response = await get(`/article/get?id=${id}`);
        if (response.code === 200) {
            const data: IArticle = response.data
            copyProperties(data, article)
            setTimeout(() => {
                generateTOC();
            }, 500)
            setTimeout(() => {
                hljs.highlightAll()
            }, 100)
        }
    } else if (name) {
        const response = await get(`/article/getByTitle?title=${name}`);
        if (response.code === 200) {
            const data: IArticle = response.data
            copyProperties(data, article)
            setTimeout(() => {
                generateTOC();
            }, 500)
            setTimeout(() => {
                hljs.highlightAll()
            }, 100)
        }
    }

}

let hasMore = ref(true)
let isShow = ref(false)
let articleContainer = reactive({})
let showToc = ref(false)
let scrollFlag = true;
let prevNode: any;

/**
 * 生成文章目录
 */
function generateTOC() {
    //评论组件
    const comment: any = document.getElementsByClassName("comment-component");
    //文章组件
    const markdown: any = document.getElementById("markdown");
    const childrenEle: any = markdown.children;
    const tocContainer: HTMLElement = document.createElement('ul');
    let scrollTopData:any=null;
    let scrollTopObj: any = [];
    for (let i = 0; i < childrenEle.length; i++) {
        const currentEle: HTMLElement = childrenEle[i];
        const tagName: any = currentEle.tagName;
        if (tagName == 'H1' || tagName == 'H2' || tagName == 'H3' || tagName == 'H4' || tagName == 'H5' || tagName == 'H6') {
            //当标签时h1-h6时,为其生成目录
            const eleItem = document.createElement('li');
            const level = tagName.match(/\d/)[0];
            const div = document.createElement('div')
            //将类容用span包围
            div.innerHTML = currentEle.innerHTML
            //并加上类名
            div.className = 'level' + level
            eleItem.appendChild(div)
            eleItem.setAttribute("class", "li-" + i)
            //将结果记录下来 方便后面的回显
            scrollTopObj[i] = currentEle.offsetTop
            //设置点击 滚动到指定地方的事件
            eleItem.addEventListener('click', () => {
                scrollTopData = currentEle.offsetTop;
                window.scrollTo({
                    top: scrollTopData,
                    behavior: "smooth"
                });
                if (prevNode) {
                    prevNode.style.color = ''
                }
                eleItem.style.color = '#3eaf7c'
                prevNode = eleItem;
                // setTimeout(() => {
                //     scrollFlag = true
                // }, 1000)
            })
            tocContainer.appendChild(eleItem)
        }
    }
    const toc: any = document.getElementById("toc");
    toc.appendChild(tocContainer)
    //*************************以上是生成目录*************************
    let prev = Date.now();
    //设置目录高亮的回显
    document.body.onscroll = ({srcElement}: any) => {
        const scroll: HTMLElement = srcElement.scrollingElement
        let now = Date.now();
        //节流
        if (now - prev > 1) {
            //do sth...
            try {
                if (tocContainer.children.length !== 0) {
                    //调整位置
                    if (scroll.scrollTop > 280) {
                        showToc.value = true;
                    } else {
                        showToc.value = false;
                    }
                    if (article.articleComment == 0 && scroll.scrollTop > (comment[0].offsetTop - scroll.clientHeight) + 300) {
                        showToc.value = false;
                    }
                }
                //滚动加载评论
                if ((scroll.scrollTop + scroll.clientHeight > scroll.scrollHeight - 50) && hasMore.value) {
                    isShow.value = true
                    page++;
                    getComments().then(() => {
                        isShow.value = !hasMore.value;
                    })
                }
                if (isBetween(scroll.scrollTop,scrollTopData,50)||!scrollTopData){
                    scrollTopData=null;
                    scrollTopObj.forEach((item: any, index: any) => {
                        if (isBetween(scroll.scrollTop,item,50)) {
                            //当满足条件时回显 目录高亮
                            if (prevNode) {
                                prevNode.style.color = ''
                            }
                            const item: any = document.querySelector(`.li-${index}`);
                            prevNode = item;
                            item.style.color = '#3eaf7c'
                            toc.scrollTo({
                                top: item.offsetTop - 300,
                                behavior: "smooth"
                            })
                            //通过异常机制退出循环
                            throw new Error("LoopTerminates");
                        }
                    })
                }
            } catch (e) {
                if (e.message !== "LoopTerminates") throw e;
            }
            prev = Date.now();
        }
    }
}

function isBetween(testVal: any, relativeValue: any, interval: number) {
    if (!testVal || !relativeValue || !interval) return false;
    return testVal < relativeValue + interval && testVal > relativeValue - interval;
}

interface ICommentItem {
    id: string | number,
    parentName: string | null
    username: string,
    content: string
    creatTime: string,
    child: Array<ICommentItem>
}

let commentNum = ref()
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
            commentNum.value = response.data.totalCount
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
    if (store.state.indexSetting.examination) {
        ElMessage({
            showClose: true,
            message: '评论成功请耐心等待管理员审核哦！'
        });
    } else {
        commentItem.length = 0
        page = 1
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

      &:hover {
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

.no-comment {
  text-align: center;
  margin: 100px auto;
  width: 850px;
  font-size: 20px;
}


.article-container {
  width: calc(100vw - 325px);
  margin: 0;
  position: relative;

  .markdown-body {
    box-sizing: border-box;
    min-width: 200px;
    max-width: 980px;
    margin: 0 auto;
    padding: 450px 0 100px 0;
  }
}

@media (max-width: 767px) {
  .markdown-body {
    padding: 15px;
  }
}

.table-of-content {
  position: fixed;
  width: 285px;
  height: calc(100vh - 120px);
  top: 100px;
  overflow: auto;
  left: 0;
  font-size: 1em;
  font-weight: 400;
  display: inline-block;
  color: #2c3e50;
  border-left: .25rem solid transparent;
  padding: 10px;
  line-height: 1.4;
  background-color: #ffffff;
  border-radius: 10px;
  overscroll-behavior: contain;
  user-select: none;

  &::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 10px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.45);
    background: #999999;
  }

  &::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 3px; /*高宽分别对应横竖滚动条的尺寸*/
    height: 10px;
  }

  :deep(ul) {
    list-style-type: none;
    line-height: 1.7;
    display: block;
    margin-block-start: 1em;
    margin-block-end: 1em;
  }

  :deep(li) {
    cursor: pointer;

    &:hover {
      color: #43AF78;
    }

    :deep(span) {
      display: block;
    }
  }

  :deep(.level1) {
    font-size: 20px;
    font-weight: 1000;
  }

  :deep(.level2) {
    margin-left: 10px;
    font-weight: revert;
  }

  :deep(.level3) {
    margin-left: 20px;
    font-weight: normal;
  }

  :deep(.level4) {
    margin-left: 30px;
    font-weight: lighter;
  }

  :deep(.level5) {
    margin-left: 40px;
    font-weight: lighter;
  }

  :deep(.level6) {
    margin-left: 50px;
    font-weight: lighter;
  }
}

.table-of-content.dark {
  color: #a0c4ff;
  background-color: #0d1117;

  &::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 10px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.45);
    background: #999999;
  }

  :deep(li) {
    cursor: pointer;

    &:hover {
      color: #43AF78;
    }

    :deep(span) {
      display: block;
    }
  }

}

@font-face {
  font-family: 'JetBrains Mono';
  src: url('../assets/JetBrainsMono-Regular.woff2') format('woff2'),
  url('../assets/JetBrainsMono-Regular.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

#markdown {
  :deep(.hljs) {
    font-family: 'JetBrains Mono';
  }
}

//目录移动动画
.slide-in-left {
  animation: slide-in-left 0.5s ease both;
}

@keyframes slide-in-left {
  0% {
    transform: translateX(-300px);
    opacity: 0;
  }
  100% {
    transform: translateX(0);
    opacity: 1;
  }
}

.slide-out-left {
  animation: slide-out-left 0.5s ease both;
}

@keyframes slide-out-left {
  0% {
    transform: translateX(0);
    opacity: 1;
  }
  100% {
    transform: translateX(-300px);
    opacity: 0;
  }
}

//文章移动动画
.to-center {
  animation: to-center 0.5s ease both;
}

@keyframes to-center {
  0% {
    transform: translateX(0);
    left: 325px;
  }
  100% {
    left: 50%;
    transform: translateX(-50%);
  }
}

.to-right {
  animation: to-right 0.5s ease both;
}

@keyframes to-right {
  0% {
    left: 50%;
    transform: translateX(-50%);
  }
  100% {
    transform: translateX(0);
    left: 325px;
  }
}

</style>
