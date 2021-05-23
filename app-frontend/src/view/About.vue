<template>
	<div class="root">
		<article class="markdown-body" id="markdown" v-html="article.content">
		</article>
	</div>
</template>

<script setup lang="ts">
import {get, loadResource} from "../http";
import {onBeforeRouteLeave} from "vue-router";
import {onMounted, reactive} from "vue";
import {copyProperties} from "../utils/common";

let article = reactive<any>({})
onMounted(async () => {
	loadStyle()
	const response = await get(`/article/get?id=1`);
	if (response.code == 200) {
		copyProperties(response.data, article)
		console.log(article)
	}
})

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
</script>

<style scoped lang="scss">
.markdown-body {
	box-sizing: border-box;
	min-width: 200px;
	max-width: 760px;
	margin: 0 auto;
	padding: 150px 0 100px 0;
}
</style>