import { RouteRecordRaw, createRouter,createWebHashHistory } from "vue-router";

const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        component:()=>import('../view/Login.vue')
    },

];

export default createRouter({
    history:createWebHashHistory(),
    routes
})

