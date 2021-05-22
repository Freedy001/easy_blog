import store from '../store/index'


export function copyProperties(source: any, dest: any) {
    Object.keys(source).forEach(key => {
        dest[key] = source[key];
    })
}

export function darkModeClass(className: string) {
    if (isDarkMode()) {
        return className + '-dark'
    } else {
        return className;
    }
}

export function addDarkClass() {
    return {'dark':isDarkMode()}
}

export function isDarkMode(){
    return store.state.darkMode
}

export function changeMode(){
    store.commit('changeDarkMode')
}