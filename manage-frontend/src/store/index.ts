import {createStore} from 'vuex'

// @ts-ignore
export default createStore({
    state: {
        articleTitle:'',
        scrollCount:0
    },
    mutations: {
        setTitle(state,title){
            state.articleTitle=title;
        },
        addScroll(state){
            state.scrollCount++;
        }
    }
})