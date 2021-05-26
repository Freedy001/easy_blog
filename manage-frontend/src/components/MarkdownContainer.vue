<template>
	<div id="editor"></div>
</template>

<script setup lang="ts">
import {defineEmit, defineProps, getCurrentInstance, onMounted, watch,toRefs} from "vue";
import Editor from '@toast-ui/editor';
import 'codemirror/lib/codemirror.css'; // Editor's Dependency Style
import '@toast-ui/editor/dist/toastui-editor.css';// Editor's Style
import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight';
import 'highlight.js/styles/github.css';
import hljs from 'highlight.js';
import '@toast-ui/editor/dist/i18n/zh-cn'
import axios from "axios";
import {loadResource} from "../http";
let editor: Editor;
const {proxy}:any = getCurrentInstance();
defineEmit(['getArticle'])
defineProps(['initText'])
onMounted(()=>{
	editor = new Editor({
		el: document.querySelector('#editor'),
		height: '85%',
		initialEditType: 'markdown',
		previewStyle: 'vertical',
		plugins: [[codeSyntaxHighlight, { hljs }]],
		language:'zh-CN',
		usageStatistics: false,
		events:{
			blur:()=>{
				proxy.$emit('getArticle',editor.getMarkdown())
			}
		},
	});
	editor.addHook("addImageBlobHook",async (file,next,c)=>{
		const authorization = localStorage.getItem("Authorization");
		let data = new FormData();
		data.append("file",file,file.name)
		const response =await axios.post(loadResource('/backend/file/upload'),data,{
			headers: {
				'Authorization': authorization
			}
		});
		next(loadResource(response.data.url),file.name)
	})
	setTimeout(()=>{
		const selector:HTMLElement = document.querySelector('.tui-editor-contents');
		// selector.style.fontSize='1.1rem'
	},500)
})



watch(()=>proxy.initText,(val)=>{
	editor.setMarkdown(val,false)
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
.tui-editor-contents{
	font-size: 15px;
	font-family: 'JetBrains Mono';
}
.CodeMirror-lines {
	font-size: 15px;
	font-family: 'JetBrains Mono';
}


</style>