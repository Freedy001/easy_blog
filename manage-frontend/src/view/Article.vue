<!--suppress JSUnresolvedVariable -->
<template>
	<div class="root">
		<div class="indexContainer">
			<div class="title-row">
				<h1>{{ articleTitle === '' ? '新文章' : articleTitle }}</h1>
				<div>
					<el-button type="danger" @click="saveDraft">保存草稿</el-button>
					<el-button type="primary" @click="publishArticle">发布</el-button>
				</div>
			</div>
			<el-input v-model="title" placeholder="请输入标题"></el-input>
		</div>
		<editor :initText="initArticle" @getArticle="getArticle"></editor>
		<ArticleSettingDrawer :id="$route.query.id"
		                      :isOpenDrawer="drawer"
		                      @saveCallback="save"
		                      @content="getContent"
		></ArticleSettingDrawer>
	</div>
</template>

<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, ref, watch} from "vue";
import editor from '../components/MarkdownContainer.vue';
import ArticleSettingDrawer from '../components/ArticleSettingDrawer.vue'
import {get, post} from "../http";
import {onBeforeRouteLeave, useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";

export default defineComponent({
	name: 'Article',
	components: {
		editor,
		ArticleSettingDrawer
	},
	setup() {
		const router = useRouter();
		const route = useRoute();
		const store = useStore();
		//是否打开侧边栏
		let drawer = ref(false)
		const {proxy}: any = getCurrentInstance();
		const articleTitle = computed(()=>store.state.articleTitle);
		let title=ref();
		watch(title,()=>{store.commit('setTitle',title)})

		let initArticle = ref<string>();
		onMounted(() => {
			if (store.state.articleContent !== '') {
				initArticle.value = store.state.articleContent
			}
		})

		/**
		 * 同步markdown文本
		 * 当修改markdown时
		 * 子组件将会通过该方法给article赋值
		 * 方便后面的传值
		 */
		function getArticle(text: string) {
			store.commit('changeArticleContent', text)
		}

		/**
		 * 对文章进行回显
		 */
		function getContent(content: string) {
			initArticle.value = content
			store.commit('changeArticleContent', content)
		}

		/**
		 * 保存或更改文章
		 * @param form
		 */
		async function save(form: any) {
			let existedTags: Array<number> = []
			let notExistedTag: Array<string> = []
			form.tagValue.forEach((value: any, index: number) => {
				if ((typeof value) == 'number') {
					existedTags.push(value)
				} else if ((typeof value) == 'string') {
					notExistedTag.push(value)
				}
			})
			const response = await post(`/article/saveOrUpdate`, {
				id: route.query.id ? route.query.id : '',
				title: form.title,
				content: store.state.articleContent,
				publishTime: form.publishTime.getTime(),
				isComment: form.isComment,
				isOverhead: form.isOverhead,
				articleStatus: form.articleStatus,
				authorId: form.authorId,
				articleCategoryId: form.category,
				articleDesc: form.desc,
				articlePoster: form.url,
				existedTags: existedTags,
				notExistedTag: notExistedTag,
			});
			if (response.code == 200) {
				proxy.$notify({
					title: '成功',
					message: '保存成功!',
					type: 'success'
				})
				initArticle.value = ''
				store.commit('changeArticleContent', '')
				store.commit('setTitle', '')
				if (route.query.id == '1') {
					await router.push('/index/setting?toForth=true');
				} else {
					await router.push('/index/articleList');
				}
			} else {
				proxy.$notify.error({
					title: '出差啦😢！',
					message: response.msg
				})
			}
		}

		/**
		 * 保存到草稿
		 */
		async function saveDraft() {
			const response = await post('/article/saveDraft', {
				id: route.query.id,//有就代表修改原来的文章和状态   无则创建新的草稿文章
				title: store.state.articleTitle,
				content: store.state.articleContent
			})
			if (response.code == 200) {
				proxy.$notify({
					title: '成功',
					message: '保存到草稿成功!',
					type: 'success'
				})
				await router.push('/index/articleList');
			} else {
				proxy.$notify.error({
					title: '出差啦😢！',
					message:response.msg
				})
			}
		}

		function publishArticle() {
			if (route.query.id == '1') {
				//id为1时 是关于页面
				save({
					publishTime: new Date(),
					tagValue: [],
					authorId: 1,
					title: '',
					isComment: false,
					isOverhead: false,
					desc: '',
					url: '',
				})
			} else {
				drawer.value = !drawer.value
			}
		}


		return {
			drawer,
			initArticle,
			articleTitle,
			title,
			save,
			getArticle,
			getContent,
			saveDraft,
			publishArticle
		}
	}
})


</script>

<style scoped lang="scss">
.root {
	height: 100%;
}

.indexContainer {
	.title-row {
		margin: 0 20px 10px 20px;
		display: flex;
		justify-content: space-between;
		max-height: 50px;
		overflow: hidden;

		h1 {
			width: 66%;
			font-size: 20px;
			align-self: center;
		}

		div {
			width: 33%;
			display: flex;
			justify-content: flex-end;
		}
	}

	.el-input {
		padding: 5px 20px 10px 5px;
		width: 100%;
	}
}

.drawer-container {
	margin: 20px;

	.item {
		margin: 20px 0;

		p {
			margin: 10px 0;
		}
	}

	h1 {
		margin: 10px 0;
		font-size: 20px;
	}

	.el-image {
		width: 100%;
		height: 100%;
		min-height: 200px;
		cursor: pointer;
	}

	.category-header {
		display: flex;
		justify-content: space-between;

		.el-button {
			height: 35px;
		}

	}

	.el-radio-group {
		margin: 10px 0;
		display: flex;
		flex-wrap: wrap;

		.el-radio {
			margin: 5px;
			width: 25%;
		}
	}

	.el-select {
		width: 100%;
	}

}

:deep(.el-textarea__inner) {
	height: 200px;
}

.full-screen {
	width: 100%;
	height: 100%;
	position: absolute;
	top: 0;
	left: 0;
	z-index: 1000;

	.boxs {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);

		height: 400px;

		.card-row {
			height: 100%;
			width: 500px;
		}
	}

	background-color: rgba(0, 0, 0, 0.9);
}

:deep(.tag-area) {
	display: none;
}

:deep(.box-card) {
	width: 100%;
}

</style>