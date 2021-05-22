import {createStore} from 'vuex'
let darkMode=localStorage.getItem('mode')||false;
// @ts-ignore
export default createStore({
    state: {
        darkMode:darkMode=='true',
        modeChanging:false,
        indexSetting:{},
        notifyReloadArticle:0,
        userCard:{}
    },
    mutations: {
        setIndexSetting(state,value){
            state.indexSetting=value;
        },
        notifyReloadArticle(state){
            state.notifyReloadArticle++;
        },
        changeDarkMode(state){
            state.darkMode=!state.darkMode;
            localStorage.setItem('mode',String(state.darkMode))
            state.modeChanging=false
        },
        changingMode(state){
            state.modeChanging=true;
        }
    }
})