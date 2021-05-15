<!--suppress ALL -->
<template>
	<div class="search-container">
		<SearchInput class="search-input" :showLogo="true"></SearchInput>
		<div class="search-result">
			<div class="search-item" v-for="result in searchResult">
				<div class="title-content">
					<h2 class="title" v-html="result.title" @click="$router.push(`article?id=${result.id}`)"></h2>
					<div class="content">
						<img :src="result.articlePoster" alt="asd" @click="$router.push(`article?id=${result.id}`)">
						<div class="details-area">
							<div v-for="hitContent in result.hitItem" class="details-item">
								<span>{{hitContent.field}}</span>
								<p  v-html="hitContent.content"></p>
							</div>
						</div>
					</div>
				</div>
				<div class="category-tag">
					<div class="category">
						<img :src="articleCategory" alt="">
						<span v-html="result.articleCategory"></span>
					</div>
					<div class="tag-container">
						<div class="teg" v-for="item in result.articleTags">
							<img :src="articleTags" alt="">
							<span v-html="item"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="empty" v-if="searchResult.length==0">
			<img :src="empty" alt="">
		 <span>TMPTY</span>
		</div>
	</div>
</template>

<script setup lang="ts">
import SearchInput from '../components/SearchInput.vue'
import {defineComponent, onMounted, reactive, watch} from "vue";
import articleCategory from "../assets/icon/articleCategory.svg";
import articleTags from "../assets/icon/articleTags.svg";
import empty from "../assets/icon/empty.svg";
import {useRoute, useRouter} from "vue-router";
import {get, loadResource} from "../http";
defineComponent({
	SearchInput
})
const router = useRouter();
const route = useRoute();
onMounted(()=>{
	if (route.query.searchString){
		search(route.query.searchString)
	}else {
		router.push('/error')
	}
})
watch(()=>route.query.searchString,(val)=>{
	search(route.query.searchString)
})
interface ISearchResult{
	"id": string|bigint,
	"title": string,
	"articleCategory":string,
	"publishTime": string,
	"articlePoster": string,
	"articleTags": Array<string>,
	"hitItem":Array<{field:string,content:string}>
}
let searchResult=reactive<Array<ISearchResult>>([])
let searchQuery:string|any;
let page=1
async function searchPage() {
	const response = await get(`/search/doSearch?searchString=${searchQuery}&page=${page}`);
	if (response.code==200){
		const data:Array<ISearchResult>=response.data;
		data.forEach((value, index) => {
			searchResult.push(value)
		})
		console.log(searchResult)
	}
}
async function search(searchString:string|any) {
	searchResult.length=0;
	page=1;
	searchQuery=searchString;
	searchPage()
}

</script>

<style scoped lang="scss">
.search-container {
	position: absolute;
	left: 0;
	width: 100%;
	top: 3%;
	height: 100%;

	.search-input {
		z-index: 500;
		position: relative;
	}

	.search-result {
		margin-top: 10px;
		position: relative;
		width: 100%;
		border-radius: 5px;
		.search-item {
			margin: 0 auto;
			width: 1000px;
			display: flex;
			justify-content: space-between;
			height: 170px;

			.title-content {
				.title {
					color: #2440b3;
					margin: 8px 5px;
					cursor: pointer;
					mark{
						color: black;
					}
				}

				.content {
					display: flex;
					align-items: center;
					width: 800px;
					overflow: hidden;
					img {
						width: 150px;
						height: 100px;
						object-fit: cover;
						border-radius: 10px;
						cursor: pointer;
					}
					.details-area{
						margin-left: 10px;
						line-height: 20px;
						height: 100px;
						span{
							font-size: 10px;
						}
						p {
							font-size: 12px;
						}
					}

				}
			}

			.category-tag {
				display: flex;
				flex-direction: column;
				width: 180px;
				overflow: auto;
				margin-top: 20px;
				.category {
					height: 20px;
					display: flex;
					align-items: center;
					span {
						margin-left: 10px;
						font-size: 14px;
					}

					img {
						width: 20px;
						height: 20px;
					}
				}

				.tag-container {
					.teg {
						height: 15px;
						display: flex;
						align-items: center;
						margin: 5px 0 5px 0;
						span {
							margin-left: 10px;
							font-size: 12px;
						}

						img {
							width: 15px;
							height: 15px;
						}
					}
				}

			}
		}
	}

	.empty{
		height: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 500px;
		img{
			width: 300px;
		}
		span{
			font-size: 100px;
			color: #322c78;
			font-family: "Times New Roman", Times, serif;
			font-weight: bold;
			user-select: none;
		}
	}
}
</style>
