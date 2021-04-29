import {createStore} from 'vuex'

export default createStore({
    state:{
        counter:0
    },
    mutations:{
        add(state){
            state.counter++;
        }
    }

})