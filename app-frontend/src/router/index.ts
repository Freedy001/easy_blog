import { RouteRecordRaw, createRouter,createWebHashHistory } from "vue-router";

const routes: Array<RouteRecordRaw> = [
    {
        path:'/',
        component:()=>import('../view/Index.vue')
    }
];

const router= createRouter({
    history:createWebHashHistory(),
    routes
})


export default router
