import {createStore} from 'vuex'

// @ts-ignore
export default createStore({
    state: {
        isLoading:false
    },
    mutations: {
        changeLoadStatus(state){
            state.isLoading=!state.isLoading;
        }
    }
})