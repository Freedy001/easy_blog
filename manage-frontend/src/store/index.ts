import {createStore} from 'vuex'
// @ts-ignore
export default createStore({
    state: {
        articleTitle:'',
        //文章markdown文本
        articleContent:'',
        scrollCount:0,
        notifyReloadReadNum:0,
        notifyReloadNickNameAndHeadImg:0,
    },
    mutations: {
        setTitle(state,title){
            state.articleTitle=title;
        },
        changeArticleContent(state,text){
            state.articleContent=text;
        },
        addScroll(state){
            state.scrollCount++;
        },
        notifyReloadReadNum(state){
            state.notifyReloadReadNum++;
        },
        notifyReloadNickNameAndHeadImg(state){
            state.notifyReloadNickNameAndHeadImg++;
        }
    }
})