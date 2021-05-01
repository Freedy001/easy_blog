import { RouteRecordRaw, createRouter,createWebHashHistory } from "vue-router";

const routes: Array<RouteRecordRaw> = [
    {
        path: "/login",
        component:()=>import('../view/Login.vue'),
        meta:{
            requireAuth: true,  // 除此路由外，其他都需登录
        }
    },
    {
        path: "/index",
        component:()=>import('../view/Index.vue'),
        children:[
            {
                path:"",
                component:()=>import('../view/Dashboard.vue')
            },
            {
                path:"article",
                component:()=>import('../view/Article.vue')
            },
            {
                path:"articleList",
                component:()=>import('../view/ArticleList.vue')
            },
            {
                path:"category&tag",
                component:()=>import('../view/Category&Tag.vue')
            },
            {
                path:"comment",
                component:()=>import('../view/Comment.vue')
            } ,
            {
                path:"user",
                component:()=>import('../view/User.vue')
            },
            {
                path:"setting",
                component:()=>import('../view/Setting.vue')
            }
        ]
    }

];

const router= createRouter({
    history:createWebHashHistory(),
    routes
})


// router.beforeEach((to, from, next) => {
//     if (!to.meta.requireAuth) {
//         if (localStorage.getItem("Authorization")) {  // 是否已登录
//             next()
//         } else {
//             next({ path: '/login' })
//         }
//     } else {
//         next()
//     }
// })

export default router
