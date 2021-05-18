import {createStore} from 'vuex'

// @ts-ignore
export default createStore({
    state: {
        articleTitle:'',
        scrollCount:0,
        notifyReloadReadNum:0,
        notifyReloadNickNameAndHeadImg:0
    },
    mutations: {
        setTitle(state,title){
            state.articleTitle=title;
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