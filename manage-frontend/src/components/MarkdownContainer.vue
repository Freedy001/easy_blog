<template>
	<div id="editor"></div>
</template>

<script setup lang="ts">
import {defineEmit, defineProps, getCurrentInstance, onMounted, watch, toRefs} from "vue";
import Editor from '@toast-ui/editor';
import 'codemirror/lib/codemirror.css'; // Editor's Dependency Style
import '@toast-ui/editor/dist/toastui-editor.css';// Editor's Style
import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight';
import 'highlight.js/styles/github.css';
import hljs from 'highlight.js';
import '@toast-ui/editor/dist/i18n/zh-cn'
import axios from "axios";
import {get, loadResource} from "../http";
import {useStore} from "vuex";

const store = useStore();
const mode: boolean = store.state.userInfo.uploadMode

let editor: Editor;
const {proxy}: any = getCurrentInstance();
defineEmit(['getArticle'])
defineProps(['initText'])
onMounted(() => {
	const editorContainer: any = document.querySelector('#editor');
	editor = new Editor({
		el: editorContainer,
		height: '85%',
		initialEditType: 'markdown',
		previewStyle: 'vertical',
		// @ts-ignore
		plugins: [[codeSyntaxHighlight, {hljs}]],
		language: 'zh-CN',
		usageStatistics: false,
		events: {
			blur: () => {
				proxy.$emit('getArticle', editor.getMarkdown())
			}
		},
	});
	editor.addHook("addImageBlobHook", async (file, next, c) => {
				if (!mode) {
					let data = new FormData();
					const response = await get(`file/getPolicy?fileName=${file.name}`);
					if (response.code == 200) {
						const res = response.data;
						data.append("policy", res.policy)
						data.append("signature", res.signature)
						data.append("ossaccessKeyId", res.accessid)
						data.append("key", res.fileName)
						data.append("dir", res.dir)
						data.append("host", res.host)
						data.append("file", file, file.name)
						await axios.post(loadResource(res.host), data);
						next(loadResource(res.fullPath), file.name)
					}
				} else {
					const authorization = localStorage.getItem("Authorization");
					let data = new FormData();
					data.append("file", file, file.name)
					const response = await axios.post(loadResource('/backend/file/upload'), data, {
						headers: {
							'Authorization': authorization
						}
					});
					next(loadResource(response.data.url), file.name)
				}
			}
	)

})

watch(() => proxy.initText, (val) => {
	editor.setMarkdown(val, false)
})


</script>

<style lang="scss">
@font-face {
	font-family: 'JetBrains Mono';
	src: url('../assets/JetBrainsMono-Regular.woff2') format('woff2'),
	url('../assets/JetBrainsMono-Regular.ttf') format('truetype');
	font-weight: normal;
	font-style: normal;
}

.tui-editor-contents {
	font-size: 15px;
	font-family: 'JetBrains Mono';
}

.CodeMirror-lines {
	font-size: 15px;
	font-family: 'JetBrains Mono';
}


</style>