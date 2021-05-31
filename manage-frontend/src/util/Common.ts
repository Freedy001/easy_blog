import {ElMessage} from "element-plus";

export function copyProperties(source: any, dest: any) {
    Object.keys(source).forEach(key => {
        dest[key] = source[key];
    })
}

export function copyPropertiesHasNull(source: any, dest: any) {
    Object.keys(source).forEach(key => {
        if (source[key] != null) {
            dest[key] = source[key];
        } else {
            if (dest[key] instanceof Array) {
                dest[key] = []
            } else if (dest[key] instanceof String) {
                dest[key] = ''
            } else if (dest[key] instanceof Number) {
                dest[key] = 0
            } else if (dest[key] instanceof Boolean) {
                dest[key] = false
            } else if (dest[key] instanceof Object) {
                dest[key] = {}
            }
        }
    })
}

export function MakeObjEmpty(any: any) {
    Object.keys(any).forEach(key => {
        if (any[key] instanceof Array) {
            any[key] = []
        } else if (any[key] instanceof String) {
            any[key] = ''
        } else if (any[key] instanceof Number) {
            any[key] = 0
        } else if (any[key] instanceof Boolean) {
            any[key] = false
        } else if (any[key] instanceof Object) {
            any[key] = {}
        }
    })
}
export function UUID(){
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
        // @ts-ignore
        return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
    })
}

let pre: any;
export function noPermission() {
    if (pre) {
        clearTimeout(pre)
        pre=null;
    }
    pre = setTimeout(() => {
        ElMessage.error({
            center: true,
            customClass: "",
            dangerouslyUseHTMLString: false,
            duration: 3000,
            iconClass: "",
            id: "",
            offset: 0,
            onClose(): void {
            },
            type: "error",
            zIndex: 0,
            showClose: true,
            message:'没有权限访问！如果你是管理员可以去(用户=>用户管理=>设置)给账户添加权限！'
        });
    }, 500);
}