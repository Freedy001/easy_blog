import { createApp } from 'vue'
import App from './App.vue'
import router from './router';
import store from './store';
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import './icon/font_2533377_hayk73dqjy8/iconfont'
import hljs from 'highlight.js';


let vueApp = createApp(App);
vueApp.use(router)
vueApp.use(store)
vueApp.use(ElementPlus)
vueApp.mount('#app')
vueApp.use(hljs.vuePlugin);
