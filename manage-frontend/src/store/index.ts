import {createStore} from 'vuex'
// @ts-ignore
export default createStore({
    state: {
        articleTitle:'',
        //文章markdown文本
        articleContent:'',
        notifyReloadReadNum:0,
        notifyReloadNickNameAndHeadImg:0,
        userInfo:{}
    },
    mutations: {
        setTitle(state,title){
            state.articleTitle=title;
        },
        changeArticleContent(state,text){
            state.articleContent=text;
        },
        notifyReloadReadNum(state){
            state.notifyReloadReadNum++;
        },
        notifyReloadNickNameAndHeadImg(state){
            state.notifyReloadNickNameAndHeadImg++;
        },
        setUserInfo(state,info){
            state.userInfo=info
        }
    }
})