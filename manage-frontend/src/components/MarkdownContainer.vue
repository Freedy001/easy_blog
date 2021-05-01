<template>
	<div id="editor" @click="click"></div>
</template>

<script setup lang="ts">
import { defineComponent, onMounted, ref} from "vue";
import Editor from '@toast-ui/editor';
import 'codemirror/lib/codemirror.css'; // Editor's Dependency Style
import '@toast-ui/editor/dist/toastui-editor.css';// Editor's Style
import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight';
import 'highlight.js/styles/github.css';
import hljs from 'highlight.js';
import '@toast-ui/editor/dist/i18n/zh-cn'
let editor: Editor;
import $ from 'jquery'

onMounted(()=>{
	editor = new Editor({
		el: document.querySelector('#editor'),
		height: '85%',
		initialEditType: 'markdown',
		previewStyle: 'vertical',
		plugins: [[codeSyntaxHighlight, { hljs }]],
		language:'zh-CN'
	});
})

function click(){
	console.log(editor.getHtml())
}

</script>

<style scoped lang="scss">
#editor{

}
</style>