import { RouteRecordRaw, createRouter,createWebHashHistory } from "vue-router";
const routes: Array<RouteRecordRaw> = [
    {
        path:'/',
        component:()=>import('../view/Index.vue')
    } ,
    {
        path:'/article',
        component:()=>import('../view/Article.vue')
    }
];


const router= createRouter({
    history:createWebHashHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        // if (savedPosition) {
        //     return  { top: 0 }
        // } else {
        //     return { top: 0 }
        // }
        return { top: 0 }
    },
})

export default router
