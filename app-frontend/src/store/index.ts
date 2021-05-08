import {createStore} from 'vuex'

// @ts-ignore
export default createStore({
    state: {
        articleTitle:''
    },
    mutations: {
        setTitle(state,title){
            state.articleTitle=title;
        }
    }

})