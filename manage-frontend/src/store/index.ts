import {createStore} from 'vuex'
// @ts-ignore
export default createStore({
    state: {
        articleTitle:'',
        scrollCount:0,
        notifyReloadReadNum:0,
        notifyReloadNickNameAndHeadImg:0,
        currentTab:'',
        clickTab:''
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
        },
        changeTab(state,tab){
            state.currentTab=tab+"*"+new Date().getTime();
        },
        changeClickTab(state,tab){
            state.clickTab=tab;
        },
        goClickTab(state){
            state.currentTab=state.clickTab;
        }
    }
})