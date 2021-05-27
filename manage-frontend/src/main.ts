import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import ElementPlus from 'element-plus'; // @ts-ignore
import 'element-plus/lib/theme-chalk/index.css';


const vueApp=createApp(App)
vueApp.use(router)
vueApp.use(store)
vueApp.use(ElementPlus)
vueApp.mount('#app')

