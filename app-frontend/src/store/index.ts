import {createStore} from 'vuex'

// @ts-ignore
export default createStore({
    state: {
        indexSetting:{},
        notifyReloadArticle:0,
    },
    mutations: {
        setIndexSetting(state,value){
            state.indexSetting=value;
        },
        notifyReloadArticle(state){
            state.notifyReloadArticle++;
        }
    }
})