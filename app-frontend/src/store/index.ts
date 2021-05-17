import {createStore} from 'vuex'

// @ts-ignore
export default createStore({
    state: {
        indexSetting:{}
    },
    mutations: {
        setIndexSetting(state,value){
            state.indexSetting=value;
        }
    }
})