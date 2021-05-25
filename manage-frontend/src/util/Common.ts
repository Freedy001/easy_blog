export function copyProperties(source: any, dest: any) {
    Object.keys(source).forEach(key => {
        dest[key] = source[key];
    })
}

export function copyPropertiesHasNull(source: any, dest: any) {
    Object.keys(source).forEach(key => {
        if (source[key]!=null) {
            dest[key] = source[key];
        }else {
            if (dest[key] instanceof Array){
                dest[key]=[]
            }else if (dest[key] instanceof String){
                dest[key]=''
            }else if (dest[key] instanceof Number){
                dest[key]=0
            }else if (dest[key] instanceof Boolean){
                dest[key]=false
            }else if (dest[key] instanceof Object){
                dest[key]= {}
            }
        }
    })
}