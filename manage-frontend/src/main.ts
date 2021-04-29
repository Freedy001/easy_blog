import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import axios from 'axios'

const vueApp=createApp(App)
vueApp.use(router)
vueApp.use(store)
vueApp.use(ElementPlus)
vueApp.mount('#app')

let axiosInstance;
if (import.meta.env.DEV){
    axiosInstance = axios.create({
        baseURL: 'https://localhost:1000/api/',
        timeout: 1000
    });
}
if (import.meta.env.PROD){
    axiosInstance=axios.create({
        baseURL: 'https://localhost/api/',
        timeout: 1000
    })
}
vueApp.config.globalProperties.$http=axiosInstance;
