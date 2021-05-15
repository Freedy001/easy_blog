import { RouteRecordRaw, createRouter,createWebHashHistory } from "vue-router";
const routes: Array<RouteRecordRaw> = [
    {
        path:'/',
        component:()=>import('../view/Index.vue')
    } ,
    {
        path:'/article',
        component:()=>import('../view/Article.vue')
    },
    {
        path:'/search',
        component:()=>import('../view/Search.vue')
    } ,
    {
        path:'/subscribe',
        component:()=>import('../view/Subscribe.vue')
    } ,
    {
        path:'/shorthand',
        component:()=>import('../view/Shorthand.vue')
    } ,
    {
        path:'/about',
        component:()=>import('../view/About.vue')
    } ,
    {
        path:'/error',
        component:()=>import('../view/ErrorPage.vue')
    }
];


const router= createRouter({
    history:createWebHashHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        return { top: 0 }
    },
})
router.beforeEach((to, from, next) => {
    console.log(123)
    if (to.matched.length===0){
        next("/error")
    }else {
        next()
    }
})


export default router
